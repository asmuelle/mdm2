import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { MeterFormService, MeterFormGroup } from './meter-form.service';
import { IMeter } from '../meter.model';
import { MeterService } from '../service/meter.service';
import { IPeer } from 'app/entities/peer/peer.model';
import { PeerService } from 'app/entities/peer/service/peer.service';
import { IProvider } from 'app/entities/provider/provider.model';
import { ProviderService } from 'app/entities/provider/service/provider.service';
import { INamespace } from 'app/entities/namespace/namespace.model';
import { NamespaceService } from 'app/entities/namespace/service/namespace.service';
import { Utility } from 'app/entities/enumerations/utility.model';
import { LoadType } from 'app/entities/enumerations/load-type.model';

@Component({
  standalone: true,
  selector: 'jhi-meter-update',
  templateUrl: './meter-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MeterUpdateComponent implements OnInit {
  isSaving = false;
  meter: IMeter | null = null;
  utilityValues = Object.keys(Utility);
  loadTypeValues = Object.keys(LoadType);

  parentsCollection: IMeter[] = [];
  alternativesCollection: IMeter[] = [];
  peersSharedCollection: IPeer[] = [];
  providersSharedCollection: IProvider[] = [];
  namespacesSharedCollection: INamespace[] = [];

  editForm: MeterFormGroup = this.meterFormService.createMeterFormGroup();

  constructor(
    protected meterService: MeterService,
    protected meterFormService: MeterFormService,
    protected peerService: PeerService,
    protected providerService: ProviderService,
    protected namespaceService: NamespaceService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareMeter = (o1: IMeter | null, o2: IMeter | null): boolean => this.meterService.compareMeter(o1, o2);

  comparePeer = (o1: IPeer | null, o2: IPeer | null): boolean => this.peerService.comparePeer(o1, o2);

  compareProvider = (o1: IProvider | null, o2: IProvider | null): boolean => this.providerService.compareProvider(o1, o2);

  compareNamespace = (o1: INamespace | null, o2: INamespace | null): boolean => this.namespaceService.compareNamespace(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ meter }) => {
      this.meter = meter;
      if (meter) {
        this.updateForm(meter);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const meter = this.meterFormService.getMeter(this.editForm);
    if (meter.id !== null) {
      this.subscribeToSaveResponse(this.meterService.update(meter));
    } else {
      this.subscribeToSaveResponse(this.meterService.create(meter));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMeter>>): void {
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

  protected updateForm(meter: IMeter): void {
    this.meter = meter;
    this.meterFormService.resetForm(this.editForm, meter);

    this.parentsCollection = this.meterService.addMeterToCollectionIfMissing<IMeter>(this.parentsCollection, meter.parent);
    this.alternativesCollection = this.meterService.addMeterToCollectionIfMissing<IMeter>(this.alternativesCollection, meter.alternative);
    this.peersSharedCollection = this.peerService.addPeerToCollectionIfMissing<IPeer>(this.peersSharedCollection, meter.peer);
    this.providersSharedCollection = this.providerService.addProviderToCollectionIfMissing<IProvider>(
      this.providersSharedCollection,
      meter.provider
    );
    this.namespacesSharedCollection = this.namespaceService.addNamespaceToCollectionIfMissing<INamespace>(
      this.namespacesSharedCollection,
      meter.namespace
    );
  }

  protected loadRelationshipsOptions(): void {
    this.meterService
      .query({ 'meterId.specified': 'false' })
      .pipe(map((res: HttpResponse<IMeter[]>) => res.body ?? []))
      .pipe(map((meters: IMeter[]) => this.meterService.addMeterToCollectionIfMissing<IMeter>(meters, this.meter?.parent)))
      .subscribe((meters: IMeter[]) => (this.parentsCollection = meters));

    this.meterService
      .query({ 'meterId.specified': 'false' })
      .pipe(map((res: HttpResponse<IMeter[]>) => res.body ?? []))
      .pipe(map((meters: IMeter[]) => this.meterService.addMeterToCollectionIfMissing<IMeter>(meters, this.meter?.alternative)))
      .subscribe((meters: IMeter[]) => (this.alternativesCollection = meters));

    this.peerService
      .query()
      .pipe(map((res: HttpResponse<IPeer[]>) => res.body ?? []))
      .pipe(map((peers: IPeer[]) => this.peerService.addPeerToCollectionIfMissing<IPeer>(peers, this.meter?.peer)))
      .subscribe((peers: IPeer[]) => (this.peersSharedCollection = peers));

    this.providerService
      .query()
      .pipe(map((res: HttpResponse<IProvider[]>) => res.body ?? []))
      .pipe(
        map((providers: IProvider[]) => this.providerService.addProviderToCollectionIfMissing<IProvider>(providers, this.meter?.provider))
      )
      .subscribe((providers: IProvider[]) => (this.providersSharedCollection = providers));

    this.namespaceService
      .query()
      .pipe(map((res: HttpResponse<INamespace[]>) => res.body ?? []))
      .pipe(
        map((namespaces: INamespace[]) =>
          this.namespaceService.addNamespaceToCollectionIfMissing<INamespace>(namespaces, this.meter?.namespace)
        )
      )
      .subscribe((namespaces: INamespace[]) => (this.namespacesSharedCollection = namespaces));
  }
}
