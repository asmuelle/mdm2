import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MeterImportFormService } from './meter-import-form.service';
import { MeterImportService } from '../service/meter-import.service';
import { IMeterImport } from '../meter-import.model';

import { MeterImportUpdateComponent } from './meter-import-update.component';

describe('MeterImport Management Update Component', () => {
  let comp: MeterImportUpdateComponent;
  let fixture: ComponentFixture<MeterImportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let meterImportFormService: MeterImportFormService;
  let meterImportService: MeterImportService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), MeterImportUpdateComponent],
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
      .overrideTemplate(MeterImportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MeterImportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    meterImportFormService = TestBed.inject(MeterImportFormService);
    meterImportService = TestBed.inject(MeterImportService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const meterImport: IMeterImport = { id: 456 };

      activatedRoute.data = of({ meterImport });
      comp.ngOnInit();

      expect(comp.meterImport).toEqual(meterImport);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMeterImport>>();
      const meterImport = { id: 123 };
      jest.spyOn(meterImportFormService, 'getMeterImport').mockReturnValue(meterImport);
      jest.spyOn(meterImportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ meterImport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: meterImport }));
      saveSubject.complete();

      // THEN
      expect(meterImportFormService.getMeterImport).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(meterImportService.update).toHaveBeenCalledWith(expect.objectContaining(meterImport));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMeterImport>>();
      const meterImport = { id: 123 };
      jest.spyOn(meterImportFormService, 'getMeterImport').mockReturnValue({ id: null });
      jest.spyOn(meterImportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ meterImport: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: meterImport }));
      saveSubject.complete();

      // THEN
      expect(meterImportFormService.getMeterImport).toHaveBeenCalled();
      expect(meterImportService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMeterImport>>();
      const meterImport = { id: 123 };
      jest.spyOn(meterImportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ meterImport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(meterImportService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
