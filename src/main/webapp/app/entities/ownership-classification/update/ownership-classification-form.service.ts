import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOwnershipClassification, NewOwnershipClassification } from '../ownership-classification.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOwnershipClassification for edit and NewOwnershipClassificationFormGroupInput for create.
 */
type OwnershipClassificationFormGroupInput = IOwnershipClassification | PartialWithRequiredKeyOf<NewOwnershipClassification>;

type OwnershipClassificationFormDefaults = Pick<NewOwnershipClassification, 'id' | 'ownerships'>;

type OwnershipClassificationFormGroupContent = {
  id: FormControl<IOwnershipClassification['id'] | NewOwnershipClassification['id']>;
  name: FormControl<IOwnershipClassification['name']>;
  ownerships: FormControl<IOwnershipClassification['ownerships']>;
};

export type OwnershipClassificationFormGroup = FormGroup<OwnershipClassificationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OwnershipClassificationFormService {
  createOwnershipClassificationFormGroup(
    ownershipClassification: OwnershipClassificationFormGroupInput = { id: null }
  ): OwnershipClassificationFormGroup {
    const ownershipClassificationRawValue = {
      ...this.getFormDefaults(),
      ...ownershipClassification,
    };
    return new FormGroup<OwnershipClassificationFormGroupContent>({
      id: new FormControl(
        { value: ownershipClassificationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(ownershipClassificationRawValue.name),
      ownerships: new FormControl(ownershipClassificationRawValue.ownerships ?? []),
    });
  }

  getOwnershipClassification(form: OwnershipClassificationFormGroup): IOwnershipClassification | NewOwnershipClassification {
    return form.getRawValue() as IOwnershipClassification | NewOwnershipClassification;
  }

  resetForm(form: OwnershipClassificationFormGroup, ownershipClassification: OwnershipClassificationFormGroupInput): void {
    const ownershipClassificationRawValue = { ...this.getFormDefaults(), ...ownershipClassification };
    form.reset(
      {
        ...ownershipClassificationRawValue,
        id: { value: ownershipClassificationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OwnershipClassificationFormDefaults {
    return {
      id: null,
      ownerships: [],
    };
  }
}
