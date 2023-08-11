import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMeter, NewMeter } from '../meter.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMeter for edit and NewMeterFormGroupInput for create.
 */
type MeterFormGroupInput = IMeter | PartialWithRequiredKeyOf<NewMeter>;

type MeterFormDefaults = Pick<NewMeter, 'id' | 'ownerships'>;

type MeterFormGroupContent = {
  id: FormControl<IMeter['id'] | NewMeter['id']>;
  name: FormControl<IMeter['name']>;
  amrWeek: FormControl<IMeter['amrWeek']>;
  amrYear: FormControl<IMeter['amrYear']>;
  utility: FormControl<IMeter['utility']>;
  loadType: FormControl<IMeter['loadType']>;
  price: FormControl<IMeter['price']>;
  lastReading: FormControl<IMeter['lastReading']>;
  contactEmail: FormControl<IMeter['contactEmail']>;
  parent: FormControl<IMeter['parent']>;
  alternative: FormControl<IMeter['alternative']>;
  peer: FormControl<IMeter['peer']>;
  provider: FormControl<IMeter['provider']>;
  namespace: FormControl<IMeter['namespace']>;
  ownerships: FormControl<IMeter['ownerships']>;
};

export type MeterFormGroup = FormGroup<MeterFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MeterFormService {
  createMeterFormGroup(meter: MeterFormGroupInput = { id: null }): MeterFormGroup {
    const meterRawValue = {
      ...this.getFormDefaults(),
      ...meter,
    };
    return new FormGroup<MeterFormGroupContent>({
      id: new FormControl(
        { value: meterRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(meterRawValue.name),
      amrWeek: new FormControl(meterRawValue.amrWeek),
      amrYear: new FormControl(meterRawValue.amrYear),
      utility: new FormControl(meterRawValue.utility),
      loadType: new FormControl(meterRawValue.loadType),
      price: new FormControl(meterRawValue.price),
      lastReading: new FormControl(meterRawValue.lastReading),
      contactEmail: new FormControl(meterRawValue.contactEmail),
      parent: new FormControl(meterRawValue.parent),
      alternative: new FormControl(meterRawValue.alternative),
      peer: new FormControl(meterRawValue.peer),
      provider: new FormControl(meterRawValue.provider),
      namespace: new FormControl(meterRawValue.namespace),
      ownerships: new FormControl(meterRawValue.ownerships ?? []),
    });
  }

  getMeter(form: MeterFormGroup): IMeter | NewMeter {
    return form.getRawValue() as IMeter | NewMeter;
  }

  resetForm(form: MeterFormGroup, meter: MeterFormGroupInput): void {
    const meterRawValue = { ...this.getFormDefaults(), ...meter };
    form.reset(
      {
        ...meterRawValue,
        id: { value: meterRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MeterFormDefaults {
    return {
      id: null,
      ownerships: [],
    };
  }
}
