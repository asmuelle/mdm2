import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ContractFormService, ContractFormGroup } from './contract-form.service';
import { IContract } from '../contract.model';
import { ContractService } from '../service/contract.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';

@Component({
  standalone: true,
  selector: 'jhi-contract-update',
  templateUrl: './contract-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ContractUpdateComponent implements OnInit {
  isSaving = false;
  contract: IContract | null = null;

  customersSharedCollection: ICustomer[] = [];

  editForm: ContractFormGroup = this.contractFormService.createContractFormGroup();

  constructor(
    protected contractService: ContractService,
    protected contractFormService: ContractFormService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contract }) => {
      this.contract = contract;
      if (contract) {
        this.updateForm(contract);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contract = this.contractFormService.getContract(this.editForm);
    if (contract.id !== null) {
      this.subscribeToSaveResponse(this.contractService.update(contract));
    } else {
      this.subscribeToSaveResponse(this.contractService.create(contract));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContract>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(contract: IContract): void {
    this.contract = contract;
    this.contractFormService.resetForm(this.editForm, contract);

    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing<ICustomer>(
      this.customersSharedCollection,
      contract.customer
    );
  }

  protected loadRelationshipsOptions(): void {
    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) =>
          this.customerService.addCustomerToCollectionIfMissing<ICustomer>(customers, this.contract?.customer)
        )
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));
  }
}
