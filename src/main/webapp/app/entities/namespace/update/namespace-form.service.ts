import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { INamespace, NewNamespace } from '../namespace.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts INamespace for edit and NewNamespaceFormGroupInput for create.
 */
type NamespaceFormGroupInput = INamespace | PartialWithRequiredKeyOf<NewNamespace>;

type NamespaceFormDefaults = Pick<NewNamespace, 'id'>;

type NamespaceFormGroupContent = {
  id: FormControl<INamespace['id'] | NewNamespace['id']>;
  name: FormControl<INamespace['name']>;
};

export type NamespaceFormGroup = FormGroup<NamespaceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class NamespaceFormService {
  createNamespaceFormGroup(namespace: NamespaceFormGroupInput = { id: null }): NamespaceFormGroup {
    const namespaceRawValue = {
      ...this.getFormDefaults(),
      ...namespace,
    };
    return new FormGroup<NamespaceFormGroupContent>({
      id: new FormControl(
        { value: namespaceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(namespaceRawValue.name, {
        validators: [Validators.required],
      }),
    });
  }

  getNamespace(form: NamespaceFormGroup): INamespace | NewNamespace {
    return form.getRawValue() as INamespace | NewNamespace;
  }

  resetForm(form: NamespaceFormGroup, namespace: NamespaceFormGroupInput): void {
    const namespaceRawValue = { ...this.getFormDefaults(), ...namespace };
    form.reset(
      {
        ...namespaceRawValue,
        id: { value: namespaceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): NamespaceFormDefaults {
    return {
      id: null,
    };
  }
}
