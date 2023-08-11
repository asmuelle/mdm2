import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { PeerFormService, PeerFormGroup } from './peer-form.service';
import { IPeer } from '../peer.model';
import { PeerService } from '../service/peer.service';
import { IOwner } from 'app/entities/owner/owner.model';
import { OwnerService } from 'app/entities/owner/service/owner.service';
import { Utility } from 'app/entities/enumerations/utility.model';
import { LoadType } from 'app/entities/enumerations/load-type.model';

@Component({
  standalone: true,
  selector: 'jhi-peer-update',
  templateUrl: './peer-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PeerUpdateComponent implements OnInit {
  isSaving = false;
  peer: IPeer | null = null;
  utilityValues = Object.keys(Utility);
  loadTypeValues = Object.keys(LoadType);

  ownersSharedCollection: IOwner[] = [];

  editForm: PeerFormGroup = this.peerFormService.createPeerFormGroup();

  constructor(
    protected peerService: PeerService,
    protected peerFormService: PeerFormService,
    protected ownerService: OwnerService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareOwner = (o1: IOwner | null, o2: IOwner | null): boolean => this.ownerService.compareOwner(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ peer }) => {
      this.peer = peer;
      if (peer) {
        this.updateForm(peer);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const peer = this.peerFormService.getPeer(this.editForm);
    if (peer.id !== null) {
      this.subscribeToSaveResponse(this.peerService.update(peer));
    } else {
      this.subscribeToSaveResponse(this.peerService.create(peer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPeer>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(peer: IPeer): void {
    this.peer = peer;
    this.peerFormService.resetForm(this.editForm, peer);

    this.ownersSharedCollection = this.ownerService.addOwnerToCollectionIfMissing<IOwner>(this.ownersSharedCollection, peer.owner);
  }

  protected loadRelationshipsOptions(): void {
    this.ownerService
      .query()
      .pipe(map((res: HttpResponse<IOwner[]>) => res.body ?? []))
      .pipe(map((owners: IOwner[]) => this.ownerService.addOwnerToCollectionIfMissing<IOwner>(owners, this.peer?.owner)))
      .subscribe((owners: IOwner[]) => (this.ownersSharedCollection = owners));
  }
}
