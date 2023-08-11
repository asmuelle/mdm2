import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { OwnershipFormService, OwnershipFormGroup } from './ownership-form.service';
import { IOwnership } from '../ownership.model';
import { OwnershipService } from '../service/ownership.service';
import { IMeter } from 'app/entities/meter/meter.model';
import { MeterService } from 'app/entities/meter/service/meter.service';
import { IOwnershipClassification } from 'app/entities/ownership-classification/ownership-classification.model';
import { OwnershipClassificationService } from 'app/entities/ownership-classification/service/ownership-classification.service';
import { IOwner } from 'app/entities/owner/owner.model';
import { OwnerService } from 'app/entities/owner/service/owner.service';

@Component({
  standalone: true,
  selector: 'jhi-ownership-update',
  templateUrl: './ownership-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OwnershipUpdateComponent implements OnInit {
  isSaving = false;
  ownership: IOwnership | null = null;

  metersSharedCollection: IMeter[] = [];
  ownershipClassificationsSharedCollection: IOwnershipClassification[] = [];
  ownersSharedCollection: IOwner[] = [];

  editForm: OwnershipFormGroup = this.ownershipFormService.createOwnershipFormGroup();

  constructor(
    protected ownershipService: OwnershipService,
    protected ownershipFormService: OwnershipFormService,
    protected meterService: MeterService,
    protected ownershipClassificationService: OwnershipClassificationService,
    protected ownerService: OwnerService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareMeter = (o1: IMeter | null, o2: IMeter | null): boolean => this.meterService.compareMeter(o1, o2);

  compareOwnershipClassification = (o1: IOwnershipClassification | null, o2: IOwnershipClassification | null): boolean =>
    this.ownershipClassificationService.compareOwnershipClassification(o1, o2);

  compareOwner = (o1: IOwner | null, o2: IOwner | null): boolean => this.ownerService.compareOwner(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ownership }) => {
      this.ownership = ownership;
      if (ownership) {
        this.updateForm(ownership);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ownership = this.ownershipFormService.getOwnership(this.editForm);
    if (ownership.id !== null) {
      this.subscribeToSaveResponse(this.ownershipService.update(ownership));
    } else {
      this.subscribeToSaveResponse(this.ownershipService.create(ownership));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOwnership>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(ownership: IOwnership): void {
    this.ownership = ownership;
    this.ownershipFormService.resetForm(this.editForm, ownership);

    this.metersSharedCollection = this.meterService.addMeterToCollectionIfMissing<IMeter>(
      this.metersSharedCollection,
      ...(ownership.meters ?? [])
    );
    this.ownershipClassificationsSharedCollection =
      this.ownershipClassificationService.addOwnershipClassificationToCollectionIfMissing<IOwnershipClassification>(
        this.ownershipClassificationsSharedCollection,
        ...(ownership.classifications ?? [])
      );
    this.ownersSharedCollection = this.ownerService.addOwnerToCollectionIfMissing<IOwner>(this.ownersSharedCollection, ownership.owner);
  }

  protected loadRelationshipsOptions(): void {
    this.meterService
      .query()
      .pipe(map((res: HttpResponse<IMeter[]>) => res.body ?? []))
      .pipe(map((meters: IMeter[]) => this.meterService.addMeterToCollectionIfMissing<IMeter>(meters, ...(this.ownership?.meters ?? []))))
      .subscribe((meters: IMeter[]) => (this.metersSharedCollection = meters));

    this.ownershipClassificationService
      .query()
      .pipe(map((res: HttpResponse<IOwnershipClassification[]>) => res.body ?? []))
      .pipe(
        map((ownershipClassifications: IOwnershipClassification[]) =>
          this.ownershipClassificationService.addOwnershipClassificationToCollectionIfMissing<IOwnershipClassification>(
            ownershipClassifications,
            ...(this.ownership?.classifications ?? [])
          )
        )
      )
      .subscribe(
        (ownershipClassifications: IOwnershipClassification[]) => (this.ownershipClassificationsSharedCollection = ownershipClassifications)
      );

    this.ownerService
      .query()
      .pipe(map((res: HttpResponse<IOwner[]>) => res.body ?? []))
      .pipe(map((owners: IOwner[]) => this.ownerService.addOwnerToCollectionIfMissing<IOwner>(owners, this.ownership?.owner)))
      .subscribe((owners: IOwner[]) => (this.ownersSharedCollection = owners));
  }
}
