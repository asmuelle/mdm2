import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { WasteTrackingParametersFormService } from './waste-tracking-parameters-form.service';
import { WasteTrackingParametersService } from '../service/waste-tracking-parameters.service';
import { IWasteTrackingParameters } from '../waste-tracking-parameters.model';

import { WasteTrackingParametersUpdateComponent } from './waste-tracking-parameters-update.component';

describe('WasteTrackingParameters Management Update Component', () => {
  let comp: WasteTrackingParametersUpdateComponent;
  let fixture: ComponentFixture<WasteTrackingParametersUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let wasteTrackingParametersFormService: WasteTrackingParametersFormService;
  let wasteTrackingParametersService: WasteTrackingParametersService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), WasteTrackingParametersUpdateComponent],
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
      .overrideTemplate(WasteTrackingParametersUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WasteTrackingParametersUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    wasteTrackingParametersFormService = TestBed.inject(WasteTrackingParametersFormService);
    wasteTrackingParametersService = TestBed.inject(WasteTrackingParametersService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const wasteTrackingParameters: IWasteTrackingParameters = { id: 456 };

      activatedRoute.data = of({ wasteTrackingParameters });
      comp.ngOnInit();

      expect(comp.wasteTrackingParameters).toEqual(wasteTrackingParameters);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWasteTrackingParameters>>();
      const wasteTrackingParameters = { id: 123 };
      jest.spyOn(wasteTrackingParametersFormService, 'getWasteTrackingParameters').mockReturnValue(wasteTrackingParameters);
      jest.spyOn(wasteTrackingParametersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wasteTrackingParameters });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: wasteTrackingParameters }));
      saveSubject.complete();

      // THEN
      expect(wasteTrackingParametersFormService.getWasteTrackingParameters).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(wasteTrackingParametersService.update).toHaveBeenCalledWith(expect.objectContaining(wasteTrackingParameters));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWasteTrackingParameters>>();
      const wasteTrackingParameters = { id: 123 };
      jest.spyOn(wasteTrackingParametersFormService, 'getWasteTrackingParameters').mockReturnValue({ id: null });
      jest.spyOn(wasteTrackingParametersService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wasteTrackingParameters: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: wasteTrackingParameters }));
      saveSubject.complete();

      // THEN
      expect(wasteTrackingParametersFormService.getWasteTrackingParameters).toHaveBeenCalled();
      expect(wasteTrackingParametersService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWasteTrackingParameters>>();
      const wasteTrackingParameters = { id: 123 };
      jest.spyOn(wasteTrackingParametersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wasteTrackingParameters });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(wasteTrackingParametersService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
