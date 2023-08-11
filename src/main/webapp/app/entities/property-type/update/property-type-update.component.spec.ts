import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PropertyTypeFormService } from './property-type-form.service';
import { PropertyTypeService } from '../service/property-type.service';
import { IPropertyType } from '../property-type.model';

import { PropertyTypeUpdateComponent } from './property-type-update.component';

describe('PropertyType Management Update Component', () => {
  let comp: PropertyTypeUpdateComponent;
  let fixture: ComponentFixture<PropertyTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let propertyTypeFormService: PropertyTypeFormService;
  let propertyTypeService: PropertyTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), PropertyTypeUpdateComponent],
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
      .overrideTemplate(PropertyTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PropertyTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    propertyTypeFormService = TestBed.inject(PropertyTypeFormService);
    propertyTypeService = TestBed.inject(PropertyTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const propertyType: IPropertyType = { id: 456 };

      activatedRoute.data = of({ propertyType });
      comp.ngOnInit();

      expect(comp.propertyType).toEqual(propertyType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPropertyType>>();
      const propertyType = { id: 123 };
      jest.spyOn(propertyTypeFormService, 'getPropertyType').mockReturnValue(propertyType);
      jest.spyOn(propertyTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ propertyType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: propertyType }));
      saveSubject.complete();

      // THEN
      expect(propertyTypeFormService.getPropertyType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(propertyTypeService.update).toHaveBeenCalledWith(expect.objectContaining(propertyType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPropertyType>>();
      const propertyType = { id: 123 };
      jest.spyOn(propertyTypeFormService, 'getPropertyType').mockReturnValue({ id: null });
      jest.spyOn(propertyTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ propertyType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: propertyType }));
      saveSubject.complete();

      // THEN
      expect(propertyTypeFormService.getPropertyType).toHaveBeenCalled();
      expect(propertyTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPropertyType>>();
      const propertyType = { id: 123 };
      jest.spyOn(propertyTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ propertyType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(propertyTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
