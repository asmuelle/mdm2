import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ContractFormService } from './contract-form.service';
import { ContractService } from '../service/contract.service';
import { IContract } from '../contract.model';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';

import { ContractUpdateComponent } from './contract-update.component';

describe('Contract Management Update Component', () => {
  let comp: ContractUpdateComponent;
  let fixture: ComponentFixture<ContractUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contractFormService: ContractFormService;
  let contractService: ContractService;
  let customerService: CustomerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ContractUpdateComponent],
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
      .overrideTemplate(ContractUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContractUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contractFormService = TestBed.inject(ContractFormService);
    contractService = TestBed.inject(ContractService);
    customerService = TestBed.inject(CustomerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Customer query and add missing value', () => {
      const contract: IContract = { id: 456 };
      const customer: ICustomer = { id: 24644 };
      contract.customer = customer;

      const customerCollection: ICustomer[] = [{ id: 3540 }];
      jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
      const additionalCustomers = [customer];
      const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
      jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      expect(customerService.query).toHaveBeenCalled();
      expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        customerCollection,
        ...additionalCustomers.map(expect.objectContaining)
      );
      expect(comp.customersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const contract: IContract = { id: 456 };
      const customer: ICustomer = { id: 3129 };
      contract.customer = customer;

      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      expect(comp.customersSharedCollection).toContain(customer);
      expect(comp.contract).toEqual(contract);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContract>>();
      const contract = { id: 123 };
      jest.spyOn(contractFormService, 'getContract').mockReturnValue(contract);
      jest.spyOn(contractService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contract }));
      saveSubject.complete();

      // THEN
      expect(contractFormService.getContract).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contractService.update).toHaveBeenCalledWith(expect.objectContaining(contract));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContract>>();
      const contract = { id: 123 };
      jest.spyOn(contractFormService, 'getContract').mockReturnValue({ id: null });
      jest.spyOn(contractService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contract: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contract }));
      saveSubject.complete();

      // THEN
      expect(contractFormService.getContract).toHaveBeenCalled();
      expect(contractService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContract>>();
      const contract = { id: 123 };
      jest.spyOn(contractService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contractService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCustomer', () => {
      it('Should forward to customerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(customerService, 'compareCustomer');
        comp.compareCustomer(entity, entity2);
        expect(customerService.compareCustomer).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
