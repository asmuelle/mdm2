import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPeer, NewPeer } from '../peer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPeer for edit and NewPeerFormGroupInput for create.
 */
type PeerFormGroupInput = IPeer | PartialWithRequiredKeyOf<NewPeer>;

type PeerFormDefaults = Pick<NewPeer, 'id'>;

type PeerFormGroupContent = {
  id: FormControl<IPeer['id'] | NewPeer['id']>;
  name: FormControl<IPeer['name']>;
  utility: FormControl<IPeer['utility']>;
  loadType: FormControl<IPeer['loadType']>;
  owner: FormControl<IPeer['owner']>;
};

export type PeerFormGroup = FormGroup<PeerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PeerFormService {
  createPeerFormGroup(peer: PeerFormGroupInput = { id: null }): PeerFormGroup {
    const peerRawValue = {
      ...this.getFormDefaults(),
      ...peer,
    };
    return new FormGroup<PeerFormGroupContent>({
      id: new FormControl(
        { value: peerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(peerRawValue.name),
      utility: new FormControl(peerRawValue.utility),
      loadType: new FormControl(peerRawValue.loadType),
      owner: new FormControl(peerRawValue.owner),
    });
  }

  getPeer(form: PeerFormGroup): IPeer | NewPeer {
    return form.getRawValue() as IPeer | NewPeer;
  }

  resetForm(form: PeerFormGroup, peer: PeerFormGroupInput): void {
    const peerRawValue = { ...this.getFormDefaults(), ...peer };
    form.reset(
      {
        ...peerRawValue,
        id: { value: peerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PeerFormDefaults {
    return {
      id: null,
    };
  }
}
