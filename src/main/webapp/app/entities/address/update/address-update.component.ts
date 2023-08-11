import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AddressFormService, AddressFormGroup } from './address-form.service';
import { IAddress } from '../address.model';
import { AddressService } from '../service/address.service';
import { IOwnership } from 'app/entities/ownership/ownership.model';
import { OwnershipService } from 'app/entities/ownership/service/ownership.service';
import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';

@Component({
  standalone: true,
  selector: 'jhi-address-update',
  templateUrl: './address-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AddressUpdateComponent implements OnInit {
  isSaving = false;
  address: IAddress | null = null;

  ownershipsCollection: IOwnership[] = [];
  countriesCollection: ICountry[] = [];

  editForm: AddressFormGroup = this.addressFormService.createAddressFormGroup();

  constructor(
    protected addressService: AddressService,
    protected addressFormService: AddressFormService,
    protected ownershipService: OwnershipService,
    protected countryService: CountryService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareOwnership = (o1: IOwnership | null, o2: IOwnership | null): boolean => this.ownershipService.compareOwnership(o1, o2);

  compareCountry = (o1: ICountry | null, o2: ICountry | null): boolean => this.countryService.compareCountry(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ address }) => {
      this.address = address;
      if (address) {
        this.updateForm(address);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const address = this.addressFormService.getAddress(this.editForm);
    if (address.id !== null) {
      this.subscribeToSaveResponse(this.addressService.update(address));
    } else {
      this.subscribeToSaveResponse(this.addressService.create(address));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAddress>>): void {
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

  protected updateForm(address: IAddress): void {
    this.address = address;
    this.addressFormService.resetForm(this.editForm, address);

    this.ownershipsCollection = this.ownershipService.addOwnershipToCollectionIfMissing<IOwnership>(
      this.ownershipsCollection,
      address.ownership
    );
    this.countriesCollection = this.countryService.addCountryToCollectionIfMissing<ICountry>(this.countriesCollection, address.country);
  }

  protected loadRelationshipsOptions(): void {
    this.ownershipService
      .query({ 'addressId.specified': 'false' })
      .pipe(map((res: HttpResponse<IOwnership[]>) => res.body ?? []))
      .pipe(
        map((ownerships: IOwnership[]) =>
          this.ownershipService.addOwnershipToCollectionIfMissing<IOwnership>(ownerships, this.address?.ownership)
        )
      )
      .subscribe((ownerships: IOwnership[]) => (this.ownershipsCollection = ownerships));

    this.countryService
      .query({ filter: 'address-is-null' })
      .pipe(map((res: HttpResponse<ICountry[]>) => res.body ?? []))
      .pipe(map((countries: ICountry[]) => this.countryService.addCountryToCollectionIfMissing<ICountry>(countries, this.address?.country)))
      .subscribe((countries: ICountry[]) => (this.countriesCollection = countries));
  }
}
