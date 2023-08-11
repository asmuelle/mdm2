import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOwnershipClassification, NewOwnershipClassification } from '../ownership-classification.model';

export type PartialUpdateOwnershipClassification = Partial<IOwnershipClassification> & Pick<IOwnershipClassification, 'id'>;

export type EntityResponseType = HttpResponse<IOwnershipClassification>;
export type EntityArrayResponseType = HttpResponse<IOwnershipClassification[]>;

@Injectable({ providedIn: 'root' })
export class OwnershipClassificationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ownership-classifications');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ownershipClassification: NewOwnershipClassification): Observable<EntityResponseType> {
    return this.http.post<IOwnershipClassification>(this.resourceUrl, ownershipClassification, { observe: 'response' });
  }

  update(ownershipClassification: IOwnershipClassification): Observable<EntityResponseType> {
    return this.http.put<IOwnershipClassification>(
      `${this.resourceUrl}/${this.getOwnershipClassificationIdentifier(ownershipClassification)}`,
      ownershipClassification,
      { observe: 'response' }
    );
  }

  partialUpdate(ownershipClassification: PartialUpdateOwnershipClassification): Observable<EntityResponseType> {
    return this.http.patch<IOwnershipClassification>(
      `${this.resourceUrl}/${this.getOwnershipClassificationIdentifier(ownershipClassification)}`,
      ownershipClassification,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOwnershipClassification>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOwnershipClassification[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOwnershipClassificationIdentifier(ownershipClassification: Pick<IOwnershipClassification, 'id'>): number {
    return ownershipClassification.id;
  }

  compareOwnershipClassification(
    o1: Pick<IOwnershipClassification, 'id'> | null,
    o2: Pick<IOwnershipClassification, 'id'> | null
  ): boolean {
    return o1 && o2 ? this.getOwnershipClassificationIdentifier(o1) === this.getOwnershipClassificationIdentifier(o2) : o1 === o2;
  }

  addOwnershipClassificationToCollectionIfMissing<Type extends Pick<IOwnershipClassification, 'id'>>(
    ownershipClassificationCollection: Type[],
    ...ownershipClassificationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ownershipClassifications: Type[] = ownershipClassificationsToCheck.filter(isPresent);
    if (ownershipClassifications.length > 0) {
      const ownershipClassificationCollectionIdentifiers = ownershipClassificationCollection.map(
        ownershipClassificationItem => this.getOwnershipClassificationIdentifier(ownershipClassificationItem)!
      );
      const ownershipClassificationsToAdd = ownershipClassifications.filter(ownershipClassificationItem => {
        const ownershipClassificationIdentifier = this.getOwnershipClassificationIdentifier(ownershipClassificationItem);
        if (ownershipClassificationCollectionIdentifiers.includes(ownershipClassificationIdentifier)) {
          return false;
        }
        ownershipClassificationCollectionIdentifiers.push(ownershipClassificationIdentifier);
        return true;
      });
      return [...ownershipClassificationsToAdd, ...ownershipClassificationCollection];
    }
    return ownershipClassificationCollection;
  }
}
