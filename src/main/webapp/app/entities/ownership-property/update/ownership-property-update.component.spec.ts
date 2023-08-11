import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OwnershipPropertyFormService } from './ownership-property-form.service';
import { OwnershipPropertyService } from '../service/ownership-property.service';
import { IOwnershipProperty } from '../ownership-property.model';
import { IPropertyType } from 'app/entities/property-type/property-type.model';
import { PropertyTypeService } from 'app/entities/property-type/service/property-type.service';
import { IOwnership } from 'app/entities/ownership/ownership.model';
import { OwnershipService } from 'app/entities/ownership/service/ownership.service';

import { OwnershipPropertyUpdateComponent } from './ownership-property-update.component';

describe('OwnershipProperty Management Update Component', () => {
  let comp: OwnershipPropertyUpdateComponent;
  let fixture: ComponentFixture<OwnershipPropertyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ownershipPropertyFormService: OwnershipPropertyFormService;
  let ownershipPropertyService: OwnershipPropertyService;
  let propertyTypeService: PropertyTypeService;
  let ownershipService: OwnershipService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), OwnershipPropertyUpdateComponent],
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
      .overrideTemplate(OwnershipPropertyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OwnershipPropertyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ownershipPropertyFormService = TestBed.inject(OwnershipPropertyFormService);
    ownershipPropertyService = TestBed.inject(OwnershipPropertyService);
    propertyTypeService = TestBed.inject(PropertyTypeService);
    ownershipService = TestBed.inject(OwnershipService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PropertyType query and add missing value', () => {
      const ownershipProperty: IOwnershipProperty = { id: 456 };
      const type: IPropertyType = { id: 27447 };
      ownershipProperty.type = type;

      const propertyTypeCollection: IPropertyType[] = [{ id: 23873 }];
      jest.spyOn(propertyTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: propertyTypeCollection })));
      const additionalPropertyTypes = [type];
      const expectedCollection: IPropertyType[] = [...additionalPropertyTypes, ...propertyTypeCollection];
      jest.spyOn(propertyTypeService, 'addPropertyTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ownershipProperty });
      comp.ngOnInit();

      expect(propertyTypeService.query).toHaveBeenCalled();
      expect(propertyTypeService.addPropertyTypeToCollectionIfMissing).toHaveBeenCalledWith(
        propertyTypeCollection,
        ...additionalPropertyTypes.map(expect.objectContaining)
      );
      expect(comp.propertyTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Ownership query and add missing value', () => {
      const ownershipProperty: IOwnershipProperty = { id: 456 };
      const ownership: IOwnership = { id: 18432 };
      ownershipProperty.ownership = ownership;

      const ownershipCollection: IOwnership[] = [{ id: 21140 }];
      jest.spyOn(ownershipService, 'query').mockReturnValue(of(new HttpResponse({ body: ownershipCollection })));
      const additionalOwnerships = [ownership];
      const expectedCollection: IOwnership[] = [...additionalOwnerships, ...ownershipCollection];
      jest.spyOn(ownershipService, 'addOwnershipToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ownershipProperty });
      comp.ngOnInit();

      expect(ownershipService.query).toHaveBeenCalled();
      expect(ownershipService.addOwnershipToCollectionIfMissing).toHaveBeenCalledWith(
        ownershipCollection,
        ...additionalOwnerships.map(expect.objectContaining)
      );
      expect(comp.ownershipsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ownershipProperty: IOwnershipProperty = { id: 456 };
      const type: IPropertyType = { id: 16276 };
      ownershipProperty.type = type;
      const ownership: IOwnership = { id: 5103 };
      ownershipProperty.ownership = ownership;

      activatedRoute.data = of({ ownershipProperty });
      comp.ngOnInit();

      expect(comp.propertyTypesSharedCollection).toContain(type);
      expect(comp.ownershipsSharedCollection).toContain(ownership);
      expect(comp.ownershipProperty).toEqual(ownershipProperty);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOwnershipProperty>>();
      const ownershipProperty = { id: 123 };
      jest.spyOn(ownershipPropertyFormService, 'getOwnershipProperty').mockReturnValue(ownershipProperty);
      jest.spyOn(ownershipPropertyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ownershipProperty });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ownershipProperty }));
      saveSubject.complete();

      // THEN
      expect(ownershipPropertyFormService.getOwnershipProperty).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ownershipPropertyService.update).toHaveBeenCalledWith(expect.objectContaining(ownershipProperty));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOwnershipProperty>>();
      const ownershipProperty = { id: 123 };
      jest.spyOn(ownershipPropertyFormService, 'getOwnershipProperty').mockReturnValue({ id: null });
      jest.spyOn(ownershipPropertyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ownershipProperty: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ownershipProperty }));
      saveSubject.complete();

      // THEN
      expect(ownershipPropertyFormService.getOwnershipProperty).toHaveBeenCalled();
      expect(ownershipPropertyService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOwnershipProperty>>();
      const ownershipProperty = { id: 123 };
      jest.spyOn(ownershipPropertyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ownershipProperty });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ownershipPropertyService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePropertyType', () => {
      it('Should forward to propertyTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(propertyTypeService, 'comparePropertyType');
        comp.comparePropertyType(entity, entity2);
        expect(propertyTypeService.comparePropertyType).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareOwnership', () => {
      it('Should forward to ownershipService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(ownershipService, 'compareOwnership');
        comp.compareOwnership(entity, entity2);
        expect(ownershipService.compareOwnership).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
