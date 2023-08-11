import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOwnershipProperty, NewOwnershipProperty } from '../ownership-property.model';

export type PartialUpdateOwnershipProperty = Partial<IOwnershipProperty> & Pick<IOwnershipProperty, 'id'>;

export type EntityResponseType = HttpResponse<IOwnershipProperty>;
export type EntityArrayResponseType = HttpResponse<IOwnershipProperty[]>;

@Injectable({ providedIn: 'root' })
export class OwnershipPropertyService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ownership-properties');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ownershipProperty: NewOwnershipProperty): Observable<EntityResponseType> {
    return this.http.post<IOwnershipProperty>(this.resourceUrl, ownershipProperty, { observe: 'response' });
  }

  update(ownershipProperty: IOwnershipProperty): Observable<EntityResponseType> {
    return this.http.put<IOwnershipProperty>(
      `${this.resourceUrl}/${this.getOwnershipPropertyIdentifier(ownershipProperty)}`,
      ownershipProperty,
      { observe: 'response' }
    );
  }

  partialUpdate(ownershipProperty: PartialUpdateOwnershipProperty): Observable<EntityResponseType> {
    return this.http.patch<IOwnershipProperty>(
      `${this.resourceUrl}/${this.getOwnershipPropertyIdentifier(ownershipProperty)}`,
      ownershipProperty,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOwnershipProperty>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOwnershipProperty[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOwnershipPropertyIdentifier(ownershipProperty: Pick<IOwnershipProperty, 'id'>): number {
    return ownershipProperty.id;
  }

  compareOwnershipProperty(o1: Pick<IOwnershipProperty, 'id'> | null, o2: Pick<IOwnershipProperty, 'id'> | null): boolean {
    return o1 && o2 ? this.getOwnershipPropertyIdentifier(o1) === this.getOwnershipPropertyIdentifier(o2) : o1 === o2;
  }

  addOwnershipPropertyToCollectionIfMissing<Type extends Pick<IOwnershipProperty, 'id'>>(
    ownershipPropertyCollection: Type[],
    ...ownershipPropertiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ownershipProperties: Type[] = ownershipPropertiesToCheck.filter(isPresent);
    if (ownershipProperties.length > 0) {
      const ownershipPropertyCollectionIdentifiers = ownershipPropertyCollection.map(
        ownershipPropertyItem => this.getOwnershipPropertyIdentifier(ownershipPropertyItem)!
      );
      const ownershipPropertiesToAdd = ownershipProperties.filter(ownershipPropertyItem => {
        const ownershipPropertyIdentifier = this.getOwnershipPropertyIdentifier(ownershipPropertyItem);
        if (ownershipPropertyCollectionIdentifiers.includes(ownershipPropertyIdentifier)) {
          return false;
        }
        ownershipPropertyCollectionIdentifiers.push(ownershipPropertyIdentifier);
        return true;
      });
      return [...ownershipPropertiesToAdd, ...ownershipPropertyCollection];
    }
    return ownershipPropertyCollection;
  }
}
