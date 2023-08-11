import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMeterImport, NewMeterImport } from '../meter-import.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMeterImport for edit and NewMeterImportFormGroupInput for create.
 */
type MeterImportFormGroupInput = IMeterImport | PartialWithRequiredKeyOf<NewMeterImport>;

type MeterImportFormDefaults = Pick<NewMeterImport, 'id'>;

type MeterImportFormGroupContent = {
  id: FormControl<IMeterImport['id'] | NewMeterImport['id']>;
  provider: FormControl<IMeterImport['provider']>;
  utility: FormControl<IMeterImport['utility']>;
  namespace: FormControl<IMeterImport['namespace']>;
  clientRef: FormControl<IMeterImport['clientRef']>;
  meterName: FormControl<IMeterImport['meterName']>;
  contactEmail: FormControl<IMeterImport['contactEmail']>;
  ownership: FormControl<IMeterImport['ownership']>;
  owner: FormControl<IMeterImport['owner']>;
  postcode: FormControl<IMeterImport['postcode']>;
  addresslines: FormControl<IMeterImport['addresslines']>;
  lat: FormControl<IMeterImport['lat']>;
  lon: FormControl<IMeterImport['lon']>;
  classifications: FormControl<IMeterImport['classifications']>;
};

export type MeterImportFormGroup = FormGroup<MeterImportFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MeterImportFormService {
  createMeterImportFormGroup(meterImport: MeterImportFormGroupInput = { id: null }): MeterImportFormGroup {
    const meterImportRawValue = {
      ...this.getFormDefaults(),
      ...meterImport,
    };
    return new FormGroup<MeterImportFormGroupContent>({
      id: new FormControl(
        { value: meterImportRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      provider: new FormControl(meterImportRawValue.provider),
      utility: new FormControl(meterImportRawValue.utility),
      namespace: new FormControl(meterImportRawValue.namespace),
      clientRef: new FormControl(meterImportRawValue.clientRef),
      meterName: new FormControl(meterImportRawValue.meterName),
      contactEmail: new FormControl(meterImportRawValue.contactEmail),
      ownership: new FormControl(meterImportRawValue.ownership),
      owner: new FormControl(meterImportRawValue.owner),
      postcode: new FormControl(meterImportRawValue.postcode),
      addresslines: new FormControl(meterImportRawValue.addresslines),
      lat: new FormControl(meterImportRawValue.lat),
      lon: new FormControl(meterImportRawValue.lon),
      classifications: new FormControl(meterImportRawValue.classifications),
    });
  }

  getMeterImport(form: MeterImportFormGroup): IMeterImport | NewMeterImport {
    return form.getRawValue() as IMeterImport | NewMeterImport;
  }

  resetForm(form: MeterImportFormGroup, meterImport: MeterImportFormGroupInput): void {
    const meterImportRawValue = { ...this.getFormDefaults(), ...meterImport };
    form.reset(
      {
        ...meterImportRawValue,
        id: { value: meterImportRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MeterImportFormDefaults {
    return {
      id: null,
    };
  }
}
