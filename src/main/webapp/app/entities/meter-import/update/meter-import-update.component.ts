import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { MeterImportFormService, MeterImportFormGroup } from './meter-import-form.service';
import { IMeterImport } from '../meter-import.model';
import { MeterImportService } from '../service/meter-import.service';
import { Utility } from 'app/entities/enumerations/utility.model';

@Component({
  standalone: true,
  selector: 'jhi-meter-import-update',
  templateUrl: './meter-import-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MeterImportUpdateComponent implements OnInit {
  isSaving = false;
  meterImport: IMeterImport | null = null;
  utilityValues = Object.keys(Utility);

  editForm: MeterImportFormGroup = this.meterImportFormService.createMeterImportFormGroup();

  constructor(
    protected meterImportService: MeterImportService,
    protected meterImportFormService: MeterImportFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ meterImport }) => {
      this.meterImport = meterImport;
      if (meterImport) {
        this.updateForm(meterImport);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const meterImport = this.meterImportFormService.getMeterImport(this.editForm);
    if (meterImport.id !== null) {
      this.subscribeToSaveResponse(this.meterImportService.update(meterImport));
    } else {
      this.subscribeToSaveResponse(this.meterImportService.create(meterImport));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMeterImport>>): void {
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

  protected updateForm(meterImport: IMeterImport): void {
    this.meterImport = meterImport;
    this.meterImportFormService.resetForm(this.editForm, meterImport);
  }
}
