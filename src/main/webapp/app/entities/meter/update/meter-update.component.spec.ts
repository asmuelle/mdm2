import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MeterFormService } from './meter-form.service';
import { MeterService } from '../service/meter.service';
import { IMeter } from '../meter.model';
import { IPeer } from 'app/entities/peer/peer.model';
import { PeerService } from 'app/entities/peer/service/peer.service';
import { IProvider } from 'app/entities/provider/provider.model';
import { ProviderService } from 'app/entities/provider/service/provider.service';
import { INamespace } from 'app/entities/namespace/namespace.model';
import { NamespaceService } from 'app/entities/namespace/service/namespace.service';

import { MeterUpdateComponent } from './meter-update.component';

describe('Meter Management Update Component', () => {
  let comp: MeterUpdateComponent;
  let fixture: ComponentFixture<MeterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let meterFormService: MeterFormService;
  let meterService: MeterService;
  let peerService: PeerService;
  let providerService: ProviderService;
  let namespaceService: NamespaceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), MeterUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MeterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MeterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    meterFormService = TestBed.inject(MeterFormService);
    meterService = TestBed.inject(MeterService);
    peerService = TestBed.inject(PeerService);
    providerService = TestBed.inject(ProviderService);
    namespaceService = TestBed.inject(NamespaceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call parent query and add missing value', () => {
      const meter: IMeter = { id: 456 };
      const parent: IMeter = { id: 20854 };
      meter.parent = parent;

      const parentCollection: IMeter[] = [{ id: 3885 }];
      jest.spyOn(meterService, 'query').mockReturnValue(of(new HttpResponse({ body: parentCollection })));
      const expectedCollection: IMeter[] = [parent, ...parentCollection];
      jest.spyOn(meterService, 'addMeterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ meter });
      comp.ngOnInit();

      expect(meterService.query).toHaveBeenCalled();
      expect(meterService.addMeterToCollectionIfMissing).toHaveBeenCalledWith(parentCollection, parent);
      expect(comp.parentsCollection).toEqual(expectedCollection);
    });

    it('Should call alternative query and add missing value', () => {
      const meter: IMeter = { id: 456 };
      const alternative: IMeter = { id: 28194 };
      meter.alternative = alternative;

      const alternativeCollection: IMeter[] = [{ id: 3958 }];
      jest.spyOn(meterService, 'query').mockReturnValue(of(new HttpResponse({ body: alternativeCollection })));
      const expectedCollection: IMeter[] = [alternative, ...alternativeCollection];
      jest.spyOn(meterService, 'addMeterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ meter });
      comp.ngOnInit();

      expect(meterService.query).toHaveBeenCalled();
      expect(meterService.addMeterToCollectionIfMissing).toHaveBeenCalledWith(alternativeCollection, alternative);
      expect(comp.alternativesCollection).toEqual(expectedCollection);
    });

    it('Should call Peer query and add missing value', () => {
      const meter: IMeter = { id: 456 };
      const peer: IPeer = { id: 11030 };
      meter.peer = peer;

      const peerCollection: IPeer[] = [{ id: 27789 }];
      jest.spyOn(peerService, 'query').mockReturnValue(of(new HttpResponse({ body: peerCollection })));
      const additionalPeers = [peer];
      const expectedCollection: IPeer[] = [...additionalPeers, ...peerCollection];
      jest.spyOn(peerService, 'addPeerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ meter });
      comp.ngOnInit();

      expect(peerService.query).toHaveBeenCalled();
      expect(peerService.addPeerToCollectionIfMissing).toHaveBeenCalledWith(
        peerCollection,
        ...additionalPeers.map(expect.objectContaining)
      );
      expect(comp.peersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Provider query and add missing value', () => {
      const meter: IMeter = { id: 456 };
      const provider: IProvider = { id: 2595 };
      meter.provider = provider;

      const providerCollection: IProvider[] = [{ id: 28382 }];
      jest.spyOn(providerService, 'query').mockReturnValue(of(new HttpResponse({ body: providerCollection })));
      const additionalProviders = [provider];
      const expectedCollection: IProvider[] = [...additionalProviders, ...providerCollection];
      jest.spyOn(providerService, 'addProviderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ meter });
      comp.ngOnInit();

      expect(providerService.query).toHaveBeenCalled();
      expect(providerService.addProviderToCollectionIfMissing).toHaveBeenCalledWith(
        providerCollection,
        ...additionalProviders.map(expect.objectContaining)
      );
      expect(comp.providersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Namespace query and add missing value', () => {
      const meter: IMeter = { id: 456 };
      const namespace: INamespace = { id: 22502 };
      meter.namespace = namespace;

      const namespaceCollection: INamespace[] = [{ id: 4533 }];
      jest.spyOn(namespaceService, 'query').mockReturnValue(of(new HttpResponse({ body: namespaceCollection })));
      const additionalNamespaces = [namespace];
      const expectedCollection: INamespace[] = [...additionalNamespaces, ...namespaceCollection];
      jest.spyOn(namespaceService, 'addNamespaceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ meter });
      comp.ngOnInit();

      expect(namespaceService.query).toHaveBeenCalled();
      expect(namespaceService.addNamespaceToCollectionIfMissing).toHaveBeenCalledWith(
        namespaceCollection,
        ...additionalNamespaces.map(expect.objectContaining)
      );
      expect(comp.namespacesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const meter: IMeter = { id: 456 };
      const parent: IMeter = { id: 29427 };
      meter.parent = parent;
      const alternative: IMeter = { id: 435 };
      meter.alternative = alternative;
      const peer: IPeer = { id: 29396 };
      meter.peer = peer;
      const provider: IProvider = { id: 12800 };
      meter.provider = provider;
      const namespace: INamespace = { id: 18065 };
      meter.namespace = namespace;

      activatedRoute.data = of({ meter });
      comp.ngOnInit();

      expect(comp.parentsCollection).toContain(parent);
      expect(comp.alternativesCollection).toContain(alternative);
      expect(comp.peersSharedCollection).toContain(peer);
      expect(comp.providersSharedCollection).toContain(provider);
      expect(comp.namespacesSharedCollection).toContain(namespace);
      expect(comp.meter).toEqual(meter);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMeter>>();
      const meter = { id: 123 };
      jest.spyOn(meterFormService, 'getMeter').mockReturnValue(meter);
      jest.spyOn(meterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ meter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: meter }));
      saveSubject.complete();

      // THEN
      expect(meterFormService.getMeter).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(meterService.update).toHaveBeenCalledWith(expect.objectContaining(meter));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMeter>>();
      const meter = { id: 123 };
      jest.spyOn(meterFormService, 'getMeter').mockReturnValue({ id: null });
      jest.spyOn(meterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ meter: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: meter }));
      saveSubject.complete();

      // THEN
      expect(meterFormService.getMeter).toHaveBeenCalled();
      expect(meterService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMeter>>();
      const meter = { id: 123 };
      jest.spyOn(meterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ meter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(meterService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMeter', () => {
      it('Should forward to meterService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(meterService, 'compareMeter');
        comp.compareMeter(entity, entity2);
        expect(meterService.compareMeter).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePeer', () => {
      it('Should forward to peerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(peerService, 'comparePeer');
        comp.comparePeer(entity, entity2);
        expect(peerService.comparePeer).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProvider', () => {
      it('Should forward to providerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(providerService, 'compareProvider');
        comp.compareProvider(entity, entity2);
        expect(providerService.compareProvider).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareNamespace', () => {
      it('Should forward to namespaceService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(namespaceService, 'compareNamespace');
        comp.compareNamespace(entity, entity2);
        expect(namespaceService.compareNamespace).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
