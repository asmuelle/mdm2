import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMeter, NewMeter } from '../meter.model';

export type PartialUpdateMeter = Partial<IMeter> & Pick<IMeter, 'id'>;

type RestOf<T extends IMeter | NewMeter> = Omit<T, 'lastReading'> & {
  lastReading?: string | null;
};

export type RestMeter = RestOf<IMeter>;

export type NewRestMeter = RestOf<NewMeter>;

export type PartialUpdateRestMeter = RestOf<PartialUpdateMeter>;

export type EntityResponseType = HttpResponse<IMeter>;
export type EntityArrayResponseType = HttpResponse<IMeter[]>;

@Injectable({ providedIn: 'root' })
export class MeterService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/meters');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(meter: NewMeter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(meter);
    return this.http.post<RestMeter>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(meter: IMeter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(meter);
    return this.http
      .put<RestMeter>(`${this.resourceUrl}/${this.getMeterIdentifier(meter)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(meter: PartialUpdateMeter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(meter);
    return this.http
      .patch<RestMeter>(`${this.resourceUrl}/${this.getMeterIdentifier(meter)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMeter>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMeter[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMeterIdentifier(meter: Pick<IMeter, 'id'>): number {
    return meter.id;
  }

  compareMeter(o1: Pick<IMeter, 'id'> | null, o2: Pick<IMeter, 'id'> | null): boolean {
    return o1 && o2 ? this.getMeterIdentifier(o1) === this.getMeterIdentifier(o2) : o1 === o2;
  }

  addMeterToCollectionIfMissing<Type extends Pick<IMeter, 'id'>>(
    meterCollection: Type[],
    ...metersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const meters: Type[] = metersToCheck.filter(isPresent);
    if (meters.length > 0) {
      const meterCollectionIdentifiers = meterCollection.map(meterItem => this.getMeterIdentifier(meterItem)!);
      const metersToAdd = meters.filter(meterItem => {
        const meterIdentifier = this.getMeterIdentifier(meterItem);
        if (meterCollectionIdentifiers.includes(meterIdentifier)) {
          return false;
        }
        meterCollectionIdentifiers.push(meterIdentifier);
        return true;
      });
      return [...metersToAdd, ...meterCollection];
    }
    return meterCollection;
  }

  protected convertDateFromClient<T extends IMeter | NewMeter | PartialUpdateMeter>(meter: T): RestOf<T> {
    return {
      ...meter,
      lastReading: meter.lastReading?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restMeter: RestMeter): IMeter {
    return {
      ...restMeter,
      lastReading: restMeter.lastReading ? dayjs(restMeter.lastReading) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMeter>): HttpResponse<IMeter> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMeter[]>): HttpResponse<IMeter[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
