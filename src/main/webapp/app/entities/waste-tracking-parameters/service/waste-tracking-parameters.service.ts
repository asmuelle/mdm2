import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWasteTrackingParameters, NewWasteTrackingParameters } from '../waste-tracking-parameters.model';

export type PartialUpdateWasteTrackingParameters = Partial<IWasteTrackingParameters> & Pick<IWasteTrackingParameters, 'id'>;

export type EntityResponseType = HttpResponse<IWasteTrackingParameters>;
export type EntityArrayResponseType = HttpResponse<IWasteTrackingParameters[]>;

@Injectable({ providedIn: 'root' })
export class WasteTrackingParametersService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/waste-tracking-parameters');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(wasteTrackingParameters: NewWasteTrackingParameters): Observable<EntityResponseType> {
    return this.http.post<IWasteTrackingParameters>(this.resourceUrl, wasteTrackingParameters, { observe: 'response' });
  }

  update(wasteTrackingParameters: IWasteTrackingParameters): Observable<EntityResponseType> {
    return this.http.put<IWasteTrackingParameters>(
      `${this.resourceUrl}/${this.getWasteTrackingParametersIdentifier(wasteTrackingParameters)}`,
      wasteTrackingParameters,
      { observe: 'response' }
    );
  }

  partialUpdate(wasteTrackingParameters: PartialUpdateWasteTrackingParameters): Observable<EntityResponseType> {
    return this.http.patch<IWasteTrackingParameters>(
      `${this.resourceUrl}/${this.getWasteTrackingParametersIdentifier(wasteTrackingParameters)}`,
      wasteTrackingParameters,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWasteTrackingParameters>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWasteTrackingParameters[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getWasteTrackingParametersIdentifier(wasteTrackingParameters: Pick<IWasteTrackingParameters, 'id'>): number {
    return wasteTrackingParameters.id;
  }

  compareWasteTrackingParameters(
    o1: Pick<IWasteTrackingParameters, 'id'> | null,
    o2: Pick<IWasteTrackingParameters, 'id'> | null
  ): boolean {
    return o1 && o2 ? this.getWasteTrackingParametersIdentifier(o1) === this.getWasteTrackingParametersIdentifier(o2) : o1 === o2;
  }

  addWasteTrackingParametersToCollectionIfMissing<Type extends Pick<IWasteTrackingParameters, 'id'>>(
    wasteTrackingParametersCollection: Type[],
    ...wasteTrackingParametersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const wasteTrackingParameters: Type[] = wasteTrackingParametersToCheck.filter(isPresent);
    if (wasteTrackingParameters.length > 0) {
      const wasteTrackingParametersCollectionIdentifiers = wasteTrackingParametersCollection.map(
        wasteTrackingParametersItem => this.getWasteTrackingParametersIdentifier(wasteTrackingParametersItem)!
      );
      const wasteTrackingParametersToAdd = wasteTrackingParameters.filter(wasteTrackingParametersItem => {
        const wasteTrackingParametersIdentifier = this.getWasteTrackingParametersIdentifier(wasteTrackingParametersItem);
        if (wasteTrackingParametersCollectionIdentifiers.includes(wasteTrackingParametersIdentifier)) {
          return false;
        }
        wasteTrackingParametersCollectionIdentifiers.push(wasteTrackingParametersIdentifier);
        return true;
      });
      return [...wasteTrackingParametersToAdd, ...wasteTrackingParametersCollection];
    }
    return wasteTrackingParametersCollection;
  }
}
