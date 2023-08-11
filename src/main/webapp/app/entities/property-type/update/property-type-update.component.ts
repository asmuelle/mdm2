import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { PropertyTypeFormService, PropertyTypeFormGroup } from './property-type-form.service';
import { IPropertyType } from '../property-type.model';
import { PropertyTypeService } from '../service/property-type.service';

@Component({
  standalone: true,
  selector: 'jhi-property-type-update',
  templateUrl: './property-type-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PropertyTypeUpdateComponent implements OnInit {
  isSaving = false;
  propertyType: IPropertyType | null = null;

  editForm: PropertyTypeFormGroup = this.propertyTypeFormService.createPropertyTypeFormGroup();

  constructor(
    protected propertyTypeService: PropertyTypeService,
    protected propertyTypeFormService: PropertyTypeFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ propertyType }) => {
      this.propertyType = propertyType;
      if (propertyType) {
        this.updateForm(propertyType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const propertyType = this.propertyTypeFormService.getPropertyType(this.editForm);
    if (propertyType.id !== null) {
      this.subscribeToSaveResponse(this.propertyTypeService.update(propertyType));
    } else {
      this.subscribeToSaveResponse(this.propertyTypeService.create(propertyType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPropertyType>>): void {
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

  protected updateForm(propertyType: IPropertyType): void {
    this.propertyType = propertyType;
    this.propertyTypeFormService.resetForm(this.editForm, propertyType);
  }
}
