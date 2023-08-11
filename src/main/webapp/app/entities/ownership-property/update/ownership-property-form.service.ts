import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOwnershipProperty, NewOwnershipProperty } from '../ownership-property.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOwnershipProperty for edit and NewOwnershipPropertyFormGroupInput for create.
 */
type OwnershipPropertyFormGroupInput = IOwnershipProperty | PartialWithRequiredKeyOf<NewOwnershipProperty>;

type OwnershipPropertyFormDefaults = Pick<NewOwnershipProperty, 'id'>;

type OwnershipPropertyFormGroupContent = {
  id: FormControl<IOwnershipProperty['id'] | NewOwnershipProperty['id']>;
  value: FormControl<IOwnershipProperty['value']>;
  type: FormControl<IOwnershipProperty['type']>;
  ownership: FormControl<IOwnershipProperty['ownership']>;
};

export type OwnershipPropertyFormGroup = FormGroup<OwnershipPropertyFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OwnershipPropertyFormService {
  createOwnershipPropertyFormGroup(ownershipProperty: OwnershipPropertyFormGroupInput = { id: null }): OwnershipPropertyFormGroup {
    const ownershipPropertyRawValue = {
      ...this.getFormDefaults(),
      ...ownershipProperty,
    };
    return new FormGroup<OwnershipPropertyFormGroupContent>({
      id: new FormControl(
        { value: ownershipPropertyRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      value: new FormControl(ownershipPropertyRawValue.value, {
        validators: [Validators.required],
      }),
      type: new FormControl(ownershipPropertyRawValue.type),
      ownership: new FormControl(ownershipPropertyRawValue.ownership),
    });
  }

  getOwnershipProperty(form: OwnershipPropertyFormGroup): IOwnershipProperty | NewOwnershipProperty {
    return form.getRawValue() as IOwnershipProperty | NewOwnershipProperty;
  }

  resetForm(form: OwnershipPropertyFormGroup, ownershipProperty: OwnershipPropertyFormGroupInput): void {
    const ownershipPropertyRawValue = { ...this.getFormDefaults(), ...ownershipProperty };
    form.reset(
      {
        ...ownershipPropertyRawValue,
        id: { value: ownershipPropertyRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OwnershipPropertyFormDefaults {
    return {
      id: null,
    };
  }
}
