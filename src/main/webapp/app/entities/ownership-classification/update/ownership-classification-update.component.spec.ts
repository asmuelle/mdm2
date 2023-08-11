import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OwnershipClassificationFormService } from './ownership-classification-form.service';
import { OwnershipClassificationService } from '../service/ownership-classification.service';
import { IOwnershipClassification } from '../ownership-classification.model';

import { OwnershipClassificationUpdateComponent } from './ownership-classification-update.component';

describe('OwnershipClassification Management Update Component', () => {
  let comp: OwnershipClassificationUpdateComponent;
  let fixture: ComponentFixture<OwnershipClassificationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ownershipClassificationFormService: OwnershipClassificationFormService;
  let ownershipClassificationService: OwnershipClassificationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), OwnershipClassificationUpdateComponent],
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
      .overrideTemplate(OwnershipClassificationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OwnershipClassificationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ownershipClassificationFormService = TestBed.inject(OwnershipClassificationFormService);
    ownershipClassificationService = TestBed.inject(OwnershipClassificationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const ownershipClassification: IOwnershipClassification = { id: 456 };

      activatedRoute.data = of({ ownershipClassification });
      comp.ngOnInit();

      expect(comp.ownershipClassification).toEqual(ownershipClassification);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOwnershipClassification>>();
      const ownershipClassification = { id: 123 };
      jest.spyOn(ownershipClassificationFormService, 'getOwnershipClassification').mockReturnValue(ownershipClassification);
      jest.spyOn(ownershipClassificationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ownershipClassification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ownershipClassification }));
      saveSubject.complete();

      // THEN
      expect(ownershipClassificationFormService.getOwnershipClassification).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ownershipClassificationService.update).toHaveBeenCalledWith(expect.objectContaining(ownershipClassification));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOwnershipClassification>>();
      const ownershipClassification = { id: 123 };
      jest.spyOn(ownershipClassificationFormService, 'getOwnershipClassification').mockReturnValue({ id: null });
      jest.spyOn(ownershipClassificationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ownershipClassification: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ownershipClassification }));
      saveSubject.complete();

      // THEN
      expect(ownershipClassificationFormService.getOwnershipClassification).toHaveBeenCalled();
      expect(ownershipClassificationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOwnershipClassification>>();
      const ownershipClassification = { id: 123 };
      jest.spyOn(ownershipClassificationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ownershipClassification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ownershipClassificationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
