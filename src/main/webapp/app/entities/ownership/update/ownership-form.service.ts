import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOwnership, NewOwnership } from '../ownership.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOwnership for edit and NewOwnershipFormGroupInput for create.
 */
type OwnershipFormGroupInput = IOwnership | PartialWithRequiredKeyOf<NewOwnership>;

type OwnershipFormDefaults = Pick<NewOwnership, 'id' | 'meters' | 'classifications'>;

type OwnershipFormGroupContent = {
  id: FormControl<IOwnership['id'] | NewOwnership['id']>;
  name: FormControl<IOwnership['name']>;
  clientRef: FormControl<IOwnership['clientRef']>;
  startDate: FormControl<IOwnership['startDate']>;
  endDate: FormControl<IOwnership['endDate']>;
  meters: FormControl<IOwnership['meters']>;
  classifications: FormControl<IOwnership['classifications']>;
  owner: FormControl<IOwnership['owner']>;
};

export type OwnershipFormGroup = FormGroup<OwnershipFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OwnershipFormService {
  createOwnershipFormGroup(ownership: OwnershipFormGroupInput = { id: null }): OwnershipFormGroup {
    const ownershipRawValue = {
      ...this.getFormDefaults(),
      ...ownership,
    };
    return new FormGroup<OwnershipFormGroupContent>({
      id: new FormControl(
        { value: ownershipRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(ownershipRawValue.name, {
        validators: [Validators.required],
      }),
      clientRef: new FormControl(ownershipRawValue.clientRef),
      startDate: new FormControl(ownershipRawValue.startDate),
      endDate: new FormControl(ownershipRawValue.endDate),
      meters: new FormControl(ownershipRawValue.meters ?? []),
      classifications: new FormControl(ownershipRawValue.classifications ?? []),
      owner: new FormControl(ownershipRawValue.owner),
    });
  }

  getOwnership(form: OwnershipFormGroup): IOwnership | NewOwnership {
    return form.getRawValue() as IOwnership | NewOwnership;
  }

  resetForm(form: OwnershipFormGroup, ownership: OwnershipFormGroupInput): void {
    const ownershipRawValue = { ...this.getFormDefaults(), ...ownership };
    form.reset(
      {
        ...ownershipRawValue,
        id: { value: ownershipRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OwnershipFormDefaults {
    return {
      id: null,
      meters: [],
      classifications: [],
    };
  }
}
