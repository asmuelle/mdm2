import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { OwnershipPropertyFormService, OwnershipPropertyFormGroup } from './ownership-property-form.service';
import { IOwnershipProperty } from '../ownership-property.model';
import { OwnershipPropertyService } from '../service/ownership-property.service';
import { IPropertyType } from 'app/entities/property-type/property-type.model';
import { PropertyTypeService } from 'app/entities/property-type/service/property-type.service';
import { IOwnership } from 'app/entities/ownership/ownership.model';
import { OwnershipService } from 'app/entities/ownership/service/ownership.service';

@Component({
  standalone: true,
  selector: 'jhi-ownership-property-update',
  templateUrl: './ownership-property-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OwnershipPropertyUpdateComponent implements OnInit {
  isSaving = false;
  ownershipProperty: IOwnershipProperty | null = null;

  propertyTypesSharedCollection: IPropertyType[] = [];
  ownershipsSharedCollection: IOwnership[] = [];

  editForm: OwnershipPropertyFormGroup = this.ownershipPropertyFormService.createOwnershipPropertyFormGroup();

  constructor(
    protected ownershipPropertyService: OwnershipPropertyService,
    protected ownershipPropertyFormService: OwnershipPropertyFormService,
    protected propertyTypeService: PropertyTypeService,
    protected ownershipService: OwnershipService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePropertyType = (o1: IPropertyType | null, o2: IPropertyType | null): boolean =>
    this.propertyTypeService.comparePropertyType(o1, o2);

  compareOwnership = (o1: IOwnership | null, o2: IOwnership | null): boolean => this.ownershipService.compareOwnership(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ownershipProperty }) => {
      this.ownershipProperty = ownershipProperty;
      if (ownershipProperty) {
        this.updateForm(ownershipProperty);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ownershipProperty = this.ownershipPropertyFormService.getOwnershipProperty(this.editForm);
    if (ownershipProperty.id !== null) {
      this.subscribeToSaveResponse(this.ownershipPropertyService.update(ownershipProperty));
    } else {
      this.subscribeToSaveResponse(this.ownershipPropertyService.create(ownershipProperty));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOwnershipProperty>>): void {
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

  protected updateForm(ownershipProperty: IOwnershipProperty): void {
    this.ownershipProperty = ownershipProperty;
    this.ownershipPropertyFormService.resetForm(this.editForm, ownershipProperty);

    this.propertyTypesSharedCollection = this.propertyTypeService.addPropertyTypeToCollectionIfMissing<IPropertyType>(
      this.propertyTypesSharedCollection,
      ownershipProperty.type
    );
    this.ownershipsSharedCollection = this.ownershipService.addOwnershipToCollectionIfMissing<IOwnership>(
      this.ownershipsSharedCollection,
      ownershipProperty.ownership
    );
  }

  protected loadRelationshipsOptions(): void {
    this.propertyTypeService
      .query()
      .pipe(map((res: HttpResponse<IPropertyType[]>) => res.body ?? []))
      .pipe(
        map((propertyTypes: IPropertyType[]) =>
          this.propertyTypeService.addPropertyTypeToCollectionIfMissing<IPropertyType>(propertyTypes, this.ownershipProperty?.type)
        )
      )
      .subscribe((propertyTypes: IPropertyType[]) => (this.propertyTypesSharedCollection = propertyTypes));

    this.ownershipService
      .query()
      .pipe(map((res: HttpResponse<IOwnership[]>) => res.body ?? []))
      .pipe(
        map((ownerships: IOwnership[]) =>
          this.ownershipService.addOwnershipToCollectionIfMissing<IOwnership>(ownerships, this.ownershipProperty?.ownership)
        )
      )
      .subscribe((ownerships: IOwnership[]) => (this.ownershipsSharedCollection = ownerships));
  }
}
