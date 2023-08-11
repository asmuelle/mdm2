import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPropertyType, NewPropertyType } from '../property-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPropertyType for edit and NewPropertyTypeFormGroupInput for create.
 */
type PropertyTypeFormGroupInput = IPropertyType | PartialWithRequiredKeyOf<NewPropertyType>;

type PropertyTypeFormDefaults = Pick<NewPropertyType, 'id'>;

type PropertyTypeFormGroupContent = {
  id: FormControl<IPropertyType['id'] | NewPropertyType['id']>;
  name: FormControl<IPropertyType['name']>;
  pattern: FormControl<IPropertyType['pattern']>;
};

export type PropertyTypeFormGroup = FormGroup<PropertyTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PropertyTypeFormService {
  createPropertyTypeFormGroup(propertyType: PropertyTypeFormGroupInput = { id: null }): PropertyTypeFormGroup {
    const propertyTypeRawValue = {
      ...this.getFormDefaults(),
      ...propertyType,
    };
    return new FormGroup<PropertyTypeFormGroupContent>({
      id: new FormControl(
        { value: propertyTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(propertyTypeRawValue.name, {
        validators: [Validators.required],
      }),
      pattern: new FormControl(propertyTypeRawValue.pattern, {
        validators: [Validators.required],
      }),
    });
  }

  getPropertyType(form: PropertyTypeFormGroup): IPropertyType | NewPropertyType {
    return form.getRawValue() as IPropertyType | NewPropertyType;
  }

  resetForm(form: PropertyTypeFormGroup, propertyType: PropertyTypeFormGroupInput): void {
    const propertyTypeRawValue = { ...this.getFormDefaults(), ...propertyType };
    form.reset(
      {
        ...propertyTypeRawValue,
        id: { value: propertyTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PropertyTypeFormDefaults {
    return {
      id: null,
    };
  }
}
