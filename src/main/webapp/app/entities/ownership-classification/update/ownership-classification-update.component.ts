import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { OwnershipClassificationFormService, OwnershipClassificationFormGroup } from './ownership-classification-form.service';
import { IOwnershipClassification } from '../ownership-classification.model';
import { OwnershipClassificationService } from '../service/ownership-classification.service';

@Component({
  standalone: true,
  selector: 'jhi-ownership-classification-update',
  templateUrl: './ownership-classification-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OwnershipClassificationUpdateComponent implements OnInit {
  isSaving = false;
  ownershipClassification: IOwnershipClassification | null = null;

  editForm: OwnershipClassificationFormGroup = this.ownershipClassificationFormService.createOwnershipClassificationFormGroup();

  constructor(
    protected ownershipClassificationService: OwnershipClassificationService,
    protected ownershipClassificationFormService: OwnershipClassificationFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ownershipClassification }) => {
      this.ownershipClassification = ownershipClassification;
      if (ownershipClassification) {
        this.updateForm(ownershipClassification);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ownershipClassification = this.ownershipClassificationFormService.getOwnershipClassification(this.editForm);
    if (ownershipClassification.id !== null) {
      this.subscribeToSaveResponse(this.ownershipClassificationService.update(ownershipClassification));
    } else {
      this.subscribeToSaveResponse(this.ownershipClassificationService.create(ownershipClassification));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOwnershipClassification>>): void {
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

  protected updateForm(ownershipClassification: IOwnershipClassification): void {
    this.ownershipClassification = ownershipClassification;
    this.ownershipClassificationFormService.resetForm(this.editForm, ownershipClassification);
  }
}
