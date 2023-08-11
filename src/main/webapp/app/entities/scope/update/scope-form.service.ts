import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IScope, NewScope } from '../scope.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IScope for edit and NewScopeFormGroupInput for create.
 */
type ScopeFormGroupInput = IScope | PartialWithRequiredKeyOf<NewScope>;

type ScopeFormDefaults = Pick<NewScope, 'id'>;

type ScopeFormGroupContent = {
  id: FormControl<IScope['id'] | NewScope['id']>;
  meterDescription: FormControl<IScope['meterDescription']>;
  meterName: FormControl<IScope['meterName']>;
  meterUtility: FormControl<IScope['meterUtility']>;
  pricePerMonth: FormControl<IScope['pricePerMonth']>;
  contract: FormControl<IScope['contract']>;
  service: FormControl<IScope['service']>;
};

export type ScopeFormGroup = FormGroup<ScopeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ScopeFormService {
  createScopeFormGroup(scope: ScopeFormGroupInput = { id: null }): ScopeFormGroup {
    const scopeRawValue = {
      ...this.getFormDefaults(),
      ...scope,
    };
    return new FormGroup<ScopeFormGroupContent>({
      id: new FormControl(
        { value: scopeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      meterDescription: new FormControl(scopeRawValue.meterDescription),
      meterName: new FormControl(scopeRawValue.meterName),
      meterUtility: new FormControl(scopeRawValue.meterUtility),
      pricePerMonth: new FormControl(scopeRawValue.pricePerMonth),
      contract: new FormControl(scopeRawValue.contract),
      service: new FormControl(scopeRawValue.service),
    });
  }

  getScope(form: ScopeFormGroup): IScope | NewScope {
    return form.getRawValue() as IScope | NewScope;
  }

  resetForm(form: ScopeFormGroup, scope: ScopeFormGroupInput): void {
    const scopeRawValue = { ...this.getFormDefaults(), ...scope };
    form.reset(
      {
        ...scopeRawValue,
        id: { value: scopeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ScopeFormDefaults {
    return {
      id: null,
    };
  }
}
