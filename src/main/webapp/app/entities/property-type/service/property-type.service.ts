import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPropertyType, NewPropertyType } from '../property-type.model';

export type PartialUpdatePropertyType = Partial<IPropertyType> & Pick<IPropertyType, 'id'>;

export type EntityResponseType = HttpResponse<IPropertyType>;
export type EntityArrayResponseType = HttpResponse<IPropertyType[]>;

@Injectable({ providedIn: 'root' })
export class PropertyTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/property-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(propertyType: NewPropertyType): Observable<EntityResponseType> {
    return this.http.post<IPropertyType>(this.resourceUrl, propertyType, { observe: 'response' });
  }

  update(propertyType: IPropertyType): Observable<EntityResponseType> {
    return this.http.put<IPropertyType>(`${this.resourceUrl}/${this.getPropertyTypeIdentifier(propertyType)}`, propertyType, {
      observe: 'response',
    });
  }

  partialUpdate(propertyType: PartialUpdatePropertyType): Observable<EntityResponseType> {
    return this.http.patch<IPropertyType>(`${this.resourceUrl}/${this.getPropertyTypeIdentifier(propertyType)}`, propertyType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPropertyType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPropertyType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPropertyTypeIdentifier(propertyType: Pick<IPropertyType, 'id'>): number {
    return propertyType.id;
  }

  comparePropertyType(o1: Pick<IPropertyType, 'id'> | null, o2: Pick<IPropertyType, 'id'> | null): boolean {
    return o1 && o2 ? this.getPropertyTypeIdentifier(o1) === this.getPropertyTypeIdentifier(o2) : o1 === o2;
  }

  addPropertyTypeToCollectionIfMissing<Type extends Pick<IPropertyType, 'id'>>(
    propertyTypeCollection: Type[],
    ...propertyTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const propertyTypes: Type[] = propertyTypesToCheck.filter(isPresent);
    if (propertyTypes.length > 0) {
      const propertyTypeCollectionIdentifiers = propertyTypeCollection.map(
        propertyTypeItem => this.getPropertyTypeIdentifier(propertyTypeItem)!
      );
      const propertyTypesToAdd = propertyTypes.filter(propertyTypeItem => {
        const propertyTypeIdentifier = this.getPropertyTypeIdentifier(propertyTypeItem);
        if (propertyTypeCollectionIdentifiers.includes(propertyTypeIdentifier)) {
          return false;
        }
        propertyTypeCollectionIdentifiers.push(propertyTypeIdentifier);
        return true;
      });
      return [...propertyTypesToAdd, ...propertyTypeCollection];
    }
    return propertyTypeCollection;
  }
}
