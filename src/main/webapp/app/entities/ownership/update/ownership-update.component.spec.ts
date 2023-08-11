import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OwnershipFormService } from './ownership-form.service';
import { OwnershipService } from '../service/ownership.service';
import { IOwnership } from '../ownership.model';
import { IMeter } from 'app/entities/meter/meter.model';
import { MeterService } from 'app/entities/meter/service/meter.service';
import { IOwnershipClassification } from 'app/entities/ownership-classification/ownership-classification.model';
import { OwnershipClassificationService } from 'app/entities/ownership-classification/service/ownership-classification.service';
import { IOwner } from 'app/entities/owner/owner.model';
import { OwnerService } from 'app/entities/owner/service/owner.service';

import { OwnershipUpdateComponent } from './ownership-update.component';

describe('Ownership Management Update Component', () => {
  let comp: OwnershipUpdateComponent;
  let fixture: ComponentFixture<OwnershipUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ownershipFormService: OwnershipFormService;
  let ownershipService: OwnershipService;
  let meterService: MeterService;
  let ownershipClassificationService: OwnershipClassificationService;
  let ownerService: OwnerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), OwnershipUpdateComponent],
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
      .overrideTemplate(OwnershipUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OwnershipUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ownershipFormService = TestBed.inject(OwnershipFormService);
    ownershipService = TestBed.inject(OwnershipService);
    meterService = TestBed.inject(MeterService);
    ownershipClassificationService = TestBed.inject(OwnershipClassificationService);
    ownerService = TestBed.inject(OwnerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Meter query and add missing value', () => {
      const ownership: IOwnership = { id: 456 };
      const meters: IMeter[] = [{ id: 8426 }];
      ownership.meters = meters;

      const meterCollection: IMeter[] = [{ id: 7172 }];
      jest.spyOn(meterService, 'query').mockReturnValue(of(new HttpResponse({ body: meterCollection })));
      const additionalMeters = [...meters];
      const expectedCollection: IMeter[] = [...additionalMeters, ...meterCollection];
      jest.spyOn(meterService, 'addMeterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ownership });
      comp.ngOnInit();

      expect(meterService.query).toHaveBeenCalled();
      expect(meterService.addMeterToCollectionIfMissing).toHaveBeenCalledWith(
        meterCollection,
        ...additionalMeters.map(expect.objectContaining)
      );
      expect(comp.metersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call OwnershipClassification query and add missing value', () => {
      const ownership: IOwnership = { id: 456 };
      const classifications: IOwnershipClassification[] = [{ id: 23887 }];
      ownership.classifications = classifications;

      const ownershipClassificationCollection: IOwnershipClassification[] = [{ id: 25364 }];
      jest
        .spyOn(ownershipClassificationService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: ownershipClassificationCollection })));
      const additionalOwnershipClassifications = [...classifications];
      const expectedCollection: IOwnershipClassification[] = [...additionalOwnershipClassifications, ...ownershipClassificationCollection];
      jest.spyOn(ownershipClassificationService, 'addOwnershipClassificationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ownership });
      comp.ngOnInit();

      expect(ownershipClassificationService.query).toHaveBeenCalled();
      expect(ownershipClassificationService.addOwnershipClassificationToCollectionIfMissing).toHaveBeenCalledWith(
        ownershipClassificationCollection,
        ...additionalOwnershipClassifications.map(expect.objectContaining)
      );
      expect(comp.ownershipClassificationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Owner query and add missing value', () => {
      const ownership: IOwnership = { id: 456 };
      const owner: IOwner = { id: 25273 };
      ownership.owner = owner;

      const ownerCollection: IOwner[] = [{ id: 15227 }];
      jest.spyOn(ownerService, 'query').mockReturnValue(of(new HttpResponse({ body: ownerCollection })));
      const additionalOwners = [owner];
      const expectedCollection: IOwner[] = [...additionalOwners, ...ownerCollection];
      jest.spyOn(ownerService, 'addOwnerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ownership });
      comp.ngOnInit();

      expect(ownerService.query).toHaveBeenCalled();
      expect(ownerService.addOwnerToCollectionIfMissing).toHaveBeenCalledWith(
        ownerCollection,
        ...additionalOwners.map(expect.objectContaining)
      );
      expect(comp.ownersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ownership: IOwnership = { id: 456 };
      const meters: IMeter = { id: 28773 };
      ownership.meters = [meters];
      const classifications: IOwnershipClassification = { id: 17947 };
      ownership.classifications = [classifications];
      const owner: IOwner = { id: 7812 };
      ownership.owner = owner;

      activatedRoute.data = of({ ownership });
      comp.ngOnInit();

      expect(comp.metersSharedCollection).toContain(meters);
      expect(comp.ownershipClassificationsSharedCollection).toContain(classifications);
      expect(comp.ownersSharedCollection).toContain(owner);
      expect(comp.ownership).toEqual(ownership);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOwnership>>();
      const ownership = { id: 123 };
      jest.spyOn(ownershipFormService, 'getOwnership').mockReturnValue(ownership);
      jest.spyOn(ownershipService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ownership });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ownership }));
      saveSubject.complete();

      // THEN
      expect(ownershipFormService.getOwnership).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ownershipService.update).toHaveBeenCalledWith(expect.objectContaining(ownership));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOwnership>>();
      const ownership = { id: 123 };
      jest.spyOn(ownershipFormService, 'getOwnership').mockReturnValue({ id: null });
      jest.spyOn(ownershipService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ownership: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ownership }));
      saveSubject.complete();

      // THEN
      expect(ownershipFormService.getOwnership).toHaveBeenCalled();
      expect(ownershipService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOwnership>>();
      const ownership = { id: 123 };
      jest.spyOn(ownershipService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ownership });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ownershipService.update).toHaveBeenCalled();
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

    describe('compareOwnershipClassification', () => {
      it('Should forward to ownershipClassificationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(ownershipClassificationService, 'compareOwnershipClassification');
        comp.compareOwnershipClassification(entity, entity2);
        expect(ownershipClassificationService.compareOwnershipClassification).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
