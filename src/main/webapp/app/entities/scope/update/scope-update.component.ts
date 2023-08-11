import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ScopeFormService, ScopeFormGroup } from './scope-form.service';
import { IScope } from '../scope.model';
import { ScopeService } from '../service/scope.service';
import { IContract } from 'app/entities/contract/contract.model';
import { ContractService } from 'app/entities/contract/service/contract.service';
import { IService } from 'app/entities/service/service.model';
import { ServiceService } from 'app/entities/service/service/service.service';

@Component({
  standalone: true,
  selector: 'jhi-scope-update',
  templateUrl: './scope-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ScopeUpdateComponent implements OnInit {
  isSaving = false;
  scope: IScope | null = null;

  contractsSharedCollection: IContract[] = [];
  servicesSharedCollection: IService[] = [];

  editForm: ScopeFormGroup = this.scopeFormService.createScopeFormGroup();

  constructor(
    protected scopeService: ScopeService,
    protected scopeFormService: ScopeFormService,
    protected contractService: ContractService,
    protected serviceService: ServiceService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareContract = (o1: IContract | null, o2: IContract | null): boolean => this.contractService.compareContract(o1, o2);

  compareService = (o1: IService | null, o2: IService | null): boolean => this.serviceService.compareService(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ scope }) => {
      this.scope = scope;
      if (scope) {
        this.updateForm(scope);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const scope = this.scopeFormService.getScope(this.editForm);
    if (scope.id !== null) {
      this.subscribeToSaveResponse(this.scopeService.update(scope));
    } else {
      this.subscribeToSaveResponse(this.scopeService.create(scope));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IScope>>): void {
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

  protected updateForm(scope: IScope): void {
    this.scope = scope;
    this.scopeFormService.resetForm(this.editForm, scope);

    this.contractsSharedCollection = this.contractService.addContractToCollectionIfMissing<IContract>(
      this.contractsSharedCollection,
      scope.contract
    );
    this.servicesSharedCollection = this.serviceService.addServiceToCollectionIfMissing<IService>(
      this.servicesSharedCollection,
      scope.service
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contractService
      .query()
      .pipe(map((res: HttpResponse<IContract[]>) => res.body ?? []))
      .pipe(
        map((contracts: IContract[]) => this.contractService.addContractToCollectionIfMissing<IContract>(contracts, this.scope?.contract))
      )
      .subscribe((contracts: IContract[]) => (this.contractsSharedCollection = contracts));

    this.serviceService
      .query()
      .pipe(map((res: HttpResponse<IService[]>) => res.body ?? []))
      .pipe(map((services: IService[]) => this.serviceService.addServiceToCollectionIfMissing<IService>(services, this.scope?.service)))
      .subscribe((services: IService[]) => (this.servicesSharedCollection = services));
  }
}
