import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { WasteTrackingParametersFormService, WasteTrackingParametersFormGroup } from './waste-tracking-parameters-form.service';
import { IWasteTrackingParameters } from '../waste-tracking-parameters.model';
import { WasteTrackingParametersService } from '../service/waste-tracking-parameters.service';

@Component({
  standalone: true,
  selector: 'jhi-waste-tracking-parameters-update',
  templateUrl: './waste-tracking-parameters-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class WasteTrackingParametersUpdateComponent implements OnInit {
  isSaving = false;
  wasteTrackingParameters: IWasteTrackingParameters | null = null;

  editForm: WasteTrackingParametersFormGroup = this.wasteTrackingParametersFormService.createWasteTrackingParametersFormGroup();

  constructor(
    protected wasteTrackingParametersService: WasteTrackingParametersService,
    protected wasteTrackingParametersFormService: WasteTrackingParametersFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wasteTrackingParameters }) => {
      this.wasteTrackingParameters = wasteTrackingParameters;
      if (wasteTrackingParameters) {
        this.updateForm(wasteTrackingParameters);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const wasteTrackingParameters = this.wasteTrackingParametersFormService.getWasteTrackingParameters(this.editForm);
    if (wasteTrackingParameters.id !== null) {
      this.subscribeToSaveResponse(this.wasteTrackingParametersService.update(wasteTrackingParameters));
    } else {
      this.subscribeToSaveResponse(this.wasteTrackingParametersService.create(wasteTrackingParameters));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWasteTrackingParameters>>): void {
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

  protected updateForm(wasteTrackingParameters: IWasteTrackingParameters): void {
    this.wasteTrackingParameters = wasteTrackingParameters;
    this.wasteTrackingParametersFormService.resetForm(this.editForm, wasteTrackingParameters);
  }
}
