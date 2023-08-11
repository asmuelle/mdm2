import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ScopeFormService } from './scope-form.service';
import { ScopeService } from '../service/scope.service';
import { IScope } from '../scope.model';
import { IContract } from 'app/entities/contract/contract.model';
import { ContractService } from 'app/entities/contract/service/contract.service';
import { IService } from 'app/entities/service/service.model';
import { ServiceService } from 'app/entities/service/service/service.service';

import { ScopeUpdateComponent } from './scope-update.component';

describe('Scope Management Update Component', () => {
  let comp: ScopeUpdateComponent;
  let fixture: ComponentFixture<ScopeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let scopeFormService: ScopeFormService;
  let scopeService: ScopeService;
  let contractService: ContractService;
  let serviceService: ServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ScopeUpdateComponent],
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
      .overrideTemplate(ScopeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ScopeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    scopeFormService = TestBed.inject(ScopeFormService);
    scopeService = TestBed.inject(ScopeService);
    contractService = TestBed.inject(ContractService);
    serviceService = TestBed.inject(ServiceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Contract query and add missing value', () => {
      const scope: IScope = { id: 456 };
      const contract: IContract = { id: 18233 };
      scope.contract = contract;

      const contractCollection: IContract[] = [{ id: 29550 }];
      jest.spyOn(contractService, 'query').mockReturnValue(of(new HttpResponse({ body: contractCollection })));
      const additionalContracts = [contract];
      const expectedCollection: IContract[] = [...additionalContracts, ...contractCollection];
      jest.spyOn(contractService, 'addContractToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ scope });
      comp.ngOnInit();

      expect(contractService.query).toHaveBeenCalled();
      expect(contractService.addContractToCollectionIfMissing).toHaveBeenCalledWith(
        contractCollection,
        ...additionalContracts.map(expect.objectContaining)
      );
      expect(comp.contractsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Service query and add missing value', () => {
      const scope: IScope = { id: 456 };
      const service: IService = { id: 32068 };
      scope.service = service;

      const serviceCollection: IService[] = [{ id: 2429 }];
      jest.spyOn(serviceService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceCollection })));
      const additionalServices = [service];
      const expectedCollection: IService[] = [...additionalServices, ...serviceCollection];
      jest.spyOn(serviceService, 'addServiceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ scope });
      comp.ngOnInit();

      expect(serviceService.query).toHaveBeenCalled();
      expect(serviceService.addServiceToCollectionIfMissing).toHaveBeenCalledWith(
        serviceCollection,
        ...additionalServices.map(expect.objectContaining)
      );
      expect(comp.servicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const scope: IScope = { id: 456 };
      const contract: IContract = { id: 24212 };
      scope.contract = contract;
      const service: IService = { id: 28686 };
      scope.service = service;

      activatedRoute.data = of({ scope });
      comp.ngOnInit();

      expect(comp.contractsSharedCollection).toContain(contract);
      expect(comp.servicesSharedCollection).toContain(service);
      expect(comp.scope).toEqual(scope);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IScope>>();
      const scope = { id: 123 };
      jest.spyOn(scopeFormService, 'getScope').mockReturnValue(scope);
      jest.spyOn(scopeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scope });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: scope }));
      saveSubject.complete();

      // THEN
      expect(scopeFormService.getScope).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(scopeService.update).toHaveBeenCalledWith(expect.objectContaining(scope));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IScope>>();
      const scope = { id: 123 };
      jest.spyOn(scopeFormService, 'getScope').mockReturnValue({ id: null });
      jest.spyOn(scopeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scope: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: scope }));
      saveSubject.complete();

      // THEN
      expect(scopeFormService.getScope).toHaveBeenCalled();
      expect(scopeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IScope>>();
      const scope = { id: 123 };
      jest.spyOn(scopeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scope });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(scopeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareContract', () => {
      it('Should forward to contractService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(contractService, 'compareContract');
        comp.compareContract(entity, entity2);
        expect(contractService.compareContract).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareService', () => {
      it('Should forward to serviceService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(serviceService, 'compareService');
        comp.compareService(entity, entity2);
        expect(serviceService.compareService).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
