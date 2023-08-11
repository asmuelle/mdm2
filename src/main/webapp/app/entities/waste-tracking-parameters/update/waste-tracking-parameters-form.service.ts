import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IWasteTrackingParameters, NewWasteTrackingParameters } from '../waste-tracking-parameters.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWasteTrackingParameters for edit and NewWasteTrackingParametersFormGroupInput for create.
 */
type WasteTrackingParametersFormGroupInput = IWasteTrackingParameters | PartialWithRequiredKeyOf<NewWasteTrackingParameters>;

type WasteTrackingParametersFormDefaults = Pick<NewWasteTrackingParameters, 'id' | 'autoCreateWasteIssues'>;

type WasteTrackingParametersFormGroupContent = {
  id: FormControl<IWasteTrackingParameters['id'] | NewWasteTrackingParameters['id']>;
  name: FormControl<IWasteTrackingParameters['name']>;
  wasteIssueCreationThreshold: FormControl<IWasteTrackingParameters['wasteIssueCreationThreshold']>;
  maxWasteIssueCreationRate: FormControl<IWasteTrackingParameters['maxWasteIssueCreationRate']>;
  maxActiveWasteIssues: FormControl<IWasteTrackingParameters['maxActiveWasteIssues']>;
  autoCreateWasteIssues: FormControl<IWasteTrackingParameters['autoCreateWasteIssues']>;
};

export type WasteTrackingParametersFormGroup = FormGroup<WasteTrackingParametersFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WasteTrackingParametersFormService {
  createWasteTrackingParametersFormGroup(
    wasteTrackingParameters: WasteTrackingParametersFormGroupInput = { id: null }
  ): WasteTrackingParametersFormGroup {
    const wasteTrackingParametersRawValue = {
      ...this.getFormDefaults(),
      ...wasteTrackingParameters,
    };
    return new FormGroup<WasteTrackingParametersFormGroupContent>({
      id: new FormControl(
        { value: wasteTrackingParametersRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(wasteTrackingParametersRawValue.name, {
        validators: [Validators.required],
      }),
      wasteIssueCreationThreshold: new FormControl(wasteTrackingParametersRawValue.wasteIssueCreationThreshold),
      maxWasteIssueCreationRate: new FormControl(wasteTrackingParametersRawValue.maxWasteIssueCreationRate),
      maxActiveWasteIssues: new FormControl(wasteTrackingParametersRawValue.maxActiveWasteIssues),
      autoCreateWasteIssues: new FormControl(wasteTrackingParametersRawValue.autoCreateWasteIssues),
    });
  }

  getWasteTrackingParameters(form: WasteTrackingParametersFormGroup): IWasteTrackingParameters | NewWasteTrackingParameters {
    return form.getRawValue() as IWasteTrackingParameters | NewWasteTrackingParameters;
  }

  resetForm(form: WasteTrackingParametersFormGroup, wasteTrackingParameters: WasteTrackingParametersFormGroupInput): void {
    const wasteTrackingParametersRawValue = { ...this.getFormDefaults(), ...wasteTrackingParameters };
    form.reset(
      {
        ...wasteTrackingParametersRawValue,
        id: { value: wasteTrackingParametersRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): WasteTrackingParametersFormDefaults {
    return {
      id: null,
      autoCreateWasteIssues: false,
    };
  }
}
