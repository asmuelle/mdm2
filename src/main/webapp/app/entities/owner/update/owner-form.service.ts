import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOwner, NewOwner } from '../owner.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOwner for edit and NewOwnerFormGroupInput for create.
 */
type OwnerFormGroupInput = IOwner | PartialWithRequiredKeyOf<NewOwner>;

type OwnerFormDefaults = Pick<NewOwner, 'id'>;

type OwnerFormGroupContent = {
  id: FormControl<IOwner['id'] | NewOwner['id']>;
  name: FormControl<IOwner['name']>;
  fullName: FormControl<IOwner['fullName']>;
  ownerKey: FormControl<IOwner['ownerKey']>;
  ownerGroup: FormControl<IOwner['ownerGroup']>;
  meters: FormControl<IOwner['meters']>;
  lastWeek: FormControl<IOwner['lastWeek']>;
  beforeLastWeek: FormControl<IOwner['beforeLastWeek']>;
  amr: FormControl<IOwner['amr']>;
  lastYear: FormControl<IOwner['lastYear']>;
  contactEmail: FormControl<IOwner['contactEmail']>;
  electricityPrice: FormControl<IOwner['electricityPrice']>;
  gasPrice: FormControl<IOwner['gasPrice']>;
  gasStage: FormControl<IOwner['gasStage']>;
  electricityStage: FormControl<IOwner['electricityStage']>;
  waterStage: FormControl<IOwner['waterStage']>;
  heatStage: FormControl<IOwner['heatStage']>;
  solarHeat: FormControl<IOwner['solarHeat']>;
  solarPowerStage: FormControl<IOwner['solarPowerStage']>;
  windStage: FormControl<IOwner['windStage']>;
  cogenPowerStage: FormControl<IOwner['cogenPowerStage']>;
};

export type OwnerFormGroup = FormGroup<OwnerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OwnerFormService {
  createOwnerFormGroup(owner: OwnerFormGroupInput = { id: null }): OwnerFormGroup {
    const ownerRawValue = {
      ...this.getFormDefaults(),
      ...owner,
    };
    return new FormGroup<OwnerFormGroupContent>({
      id: new FormControl(
        { value: ownerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(ownerRawValue.name, {
        validators: [Validators.required],
      }),
      fullName: new FormControl(ownerRawValue.fullName),
      ownerKey: new FormControl(ownerRawValue.ownerKey),
      ownerGroup: new FormControl(ownerRawValue.ownerGroup),
      meters: new FormControl(ownerRawValue.meters),
      lastWeek: new FormControl(ownerRawValue.lastWeek),
      beforeLastWeek: new FormControl(ownerRawValue.beforeLastWeek),
      amr: new FormControl(ownerRawValue.amr),
      lastYear: new FormControl(ownerRawValue.lastYear),
      contactEmail: new FormControl(ownerRawValue.contactEmail),
      electricityPrice: new FormControl(ownerRawValue.electricityPrice),
      gasPrice: new FormControl(ownerRawValue.gasPrice),
      gasStage: new FormControl(ownerRawValue.gasStage),
      electricityStage: new FormControl(ownerRawValue.electricityStage),
      waterStage: new FormControl(ownerRawValue.waterStage),
      heatStage: new FormControl(ownerRawValue.heatStage),
      solarHeat: new FormControl(ownerRawValue.solarHeat),
      solarPowerStage: new FormControl(ownerRawValue.solarPowerStage),
      windStage: new FormControl(ownerRawValue.windStage),
      cogenPowerStage: new FormControl(ownerRawValue.cogenPowerStage),
    });
  }

  getOwner(form: OwnerFormGroup): IOwner | NewOwner {
    return form.getRawValue() as IOwner | NewOwner;
  }

  resetForm(form: OwnerFormGroup, owner: OwnerFormGroupInput): void {
    const ownerRawValue = { ...this.getFormDefaults(), ...owner };
    form.reset(
      {
        ...ownerRawValue,
        id: { value: ownerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OwnerFormDefaults {
    return {
      id: null,
    };
  }
}
