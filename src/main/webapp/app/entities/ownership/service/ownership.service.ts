import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOwnership, NewOwnership } from '../ownership.model';

export type PartialUpdateOwnership = Partial<IOwnership> & Pick<IOwnership, 'id'>;

type RestOf<T extends IOwnership | NewOwnership> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestOwnership = RestOf<IOwnership>;

export type NewRestOwnership = RestOf<NewOwnership>;

export type PartialUpdateRestOwnership = RestOf<PartialUpdateOwnership>;

export type EntityResponseType = HttpResponse<IOwnership>;
export type EntityArrayResponseType = HttpResponse<IOwnership[]>;

@Injectable({ providedIn: 'root' })
export class OwnershipService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ownerships');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ownership: NewOwnership): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ownership);
    return this.http
      .post<RestOwnership>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(ownership: IOwnership): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ownership);
    return this.http
      .put<RestOwnership>(`${this.resourceUrl}/${this.getOwnershipIdentifier(ownership)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(ownership: PartialUpdateOwnership): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ownership);
    return this.http
      .patch<RestOwnership>(`${this.resourceUrl}/${this.getOwnershipIdentifier(ownership)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestOwnership>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOwnership[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOwnershipIdentifier(ownership: Pick<IOwnership, 'id'>): number {
    return ownership.id;
  }

  compareOwnership(o1: Pick<IOwnership, 'id'> | null, o2: Pick<IOwnership, 'id'> | null): boolean {
    return o1 && o2 ? this.getOwnershipIdentifier(o1) === this.getOwnershipIdentifier(o2) : o1 === o2;
  }

  addOwnershipToCollectionIfMissing<Type extends Pick<IOwnership, 'id'>>(
    ownershipCollection: Type[],
    ...ownershipsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ownerships: Type[] = ownershipsToCheck.filter(isPresent);
    if (ownerships.length > 0) {
      const ownershipCollectionIdentifiers = ownershipCollection.map(ownershipItem => this.getOwnershipIdentifier(ownershipItem)!);
      const ownershipsToAdd = ownerships.filter(ownershipItem => {
        const ownershipIdentifier = this.getOwnershipIdentifier(ownershipItem);
        if (ownershipCollectionIdentifiers.includes(ownershipIdentifier)) {
          return false;
        }
        ownershipCollectionIdentifiers.push(ownershipIdentifier);
        return true;
      });
      return [...ownershipsToAdd, ...ownershipCollection];
    }
    return ownershipCollection;
  }

  protected convertDateFromClient<T extends IOwnership | NewOwnership | PartialUpdateOwnership>(ownership: T): RestOf<T> {
    return {
      ...ownership,
      startDate: ownership.startDate?.format(DATE_FORMAT) ?? null,
      endDate: ownership.endDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restOwnership: RestOwnership): IOwnership {
    return {
      ...restOwnership,
      startDate: restOwnership.startDate ? dayjs(restOwnership.startDate) : undefined,
      endDate: restOwnership.endDate ? dayjs(restOwnership.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestOwnership>): HttpResponse<IOwnership> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestOwnership[]>): HttpResponse<IOwnership[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
