import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { NamespaceFormService } from './namespace-form.service';
import { NamespaceService } from '../service/namespace.service';
import { INamespace } from '../namespace.model';

import { NamespaceUpdateComponent } from './namespace-update.component';

describe('Namespace Management Update Component', () => {
  let comp: NamespaceUpdateComponent;
  let fixture: ComponentFixture<NamespaceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let namespaceFormService: NamespaceFormService;
  let namespaceService: NamespaceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), NamespaceUpdateComponent],
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
      .overrideTemplate(NamespaceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NamespaceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    namespaceFormService = TestBed.inject(NamespaceFormService);
    namespaceService = TestBed.inject(NamespaceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const namespace: INamespace = { id: 456 };

      activatedRoute.data = of({ namespace });
      comp.ngOnInit();

      expect(comp.namespace).toEqual(namespace);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INamespace>>();
      const namespace = { id: 123 };
      jest.spyOn(namespaceFormService, 'getNamespace').mockReturnValue(namespace);
      jest.spyOn(namespaceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ namespace });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: namespace }));
      saveSubject.complete();

      // THEN
      expect(namespaceFormService.getNamespace).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(namespaceService.update).toHaveBeenCalledWith(expect.objectContaining(namespace));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INamespace>>();
      const namespace = { id: 123 };
      jest.spyOn(namespaceFormService, 'getNamespace').mockReturnValue({ id: null });
      jest.spyOn(namespaceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ namespace: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: namespace }));
      saveSubject.complete();

      // THEN
      expect(namespaceFormService.getNamespace).toHaveBeenCalled();
      expect(namespaceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INamespace>>();
      const namespace = { id: 123 };
      jest.spyOn(namespaceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ namespace });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(namespaceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
