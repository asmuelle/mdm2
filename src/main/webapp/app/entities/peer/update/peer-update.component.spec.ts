import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PeerFormService } from './peer-form.service';
import { PeerService } from '../service/peer.service';
import { IPeer } from '../peer.model';
import { IOwner } from 'app/entities/owner/owner.model';
import { OwnerService } from 'app/entities/owner/service/owner.service';

import { PeerUpdateComponent } from './peer-update.component';

describe('Peer Management Update Component', () => {
  let comp: PeerUpdateComponent;
  let fixture: ComponentFixture<PeerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let peerFormService: PeerFormService;
  let peerService: PeerService;
  let ownerService: OwnerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), PeerUpdateComponent],
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
      .overrideTemplate(PeerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PeerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    peerFormService = TestBed.inject(PeerFormService);
    peerService = TestBed.inject(PeerService);
    ownerService = TestBed.inject(OwnerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Owner query and add missing value', () => {
      const peer: IPeer = { id: 456 };
      const owner: IOwner = { id: 25156 };
      peer.owner = owner;

      const ownerCollection: IOwner[] = [{ id: 27036 }];
      jest.spyOn(ownerService, 'query').mockReturnValue(of(new HttpResponse({ body: ownerCollection })));
      const additionalOwners = [owner];
      const expectedCollection: IOwner[] = [...additionalOwners, ...ownerCollection];
      jest.spyOn(ownerService, 'addOwnerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ peer });
      comp.ngOnInit();

      expect(ownerService.query).toHaveBeenCalled();
      expect(ownerService.addOwnerToCollectionIfMissing).toHaveBeenCalledWith(
        ownerCollection,
        ...additionalOwners.map(expect.objectContaining)
      );
      expect(comp.ownersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const peer: IPeer = { id: 456 };
      const owner: IOwner = { id: 10477 };
      peer.owner = owner;

      activatedRoute.data = of({ peer });
      comp.ngOnInit();

      expect(comp.ownersSharedCollection).toContain(owner);
      expect(comp.peer).toEqual(peer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPeer>>();
      const peer = { id: 123 };
      jest.spyOn(peerFormService, 'getPeer').mockReturnValue(peer);
      jest.spyOn(peerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ peer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: peer }));
      saveSubject.complete();

      // THEN
      expect(peerFormService.getPeer).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(peerService.update).toHaveBeenCalledWith(expect.objectContaining(peer));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPeer>>();
      const peer = { id: 123 };
      jest.spyOn(peerFormService, 'getPeer').mockReturnValue({ id: null });
      jest.spyOn(peerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ peer: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: peer }));
      saveSubject.complete();

      // THEN
      expect(peerFormService.getPeer).toHaveBeenCalled();
      expect(peerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPeer>>();
      const peer = { id: 123 };
      jest.spyOn(peerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ peer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(peerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareOwner', () => {
      it('Should forward to ownerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(ownerService, 'compareOwner');
        comp.compareOwner(entity, entity2);
        expect(ownerService.compareOwner).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
