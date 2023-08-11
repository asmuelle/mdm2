import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMeterImport, NewMeterImport } from '../meter-import.model';

export type PartialUpdateMeterImport = Partial<IMeterImport> & Pick<IMeterImport, 'id'>;

export type EntityResponseType = HttpResponse<IMeterImport>;
export type EntityArrayResponseType = HttpResponse<IMeterImport[]>;

@Injectable({ providedIn: 'root' })
export class MeterImportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/meter-imports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(meterImport: NewMeterImport): Observable<EntityResponseType> {
    return this.http.post<IMeterImport>(this.resourceUrl, meterImport, { observe: 'response' });
  }

  update(meterImport: IMeterImport): Observable<EntityResponseType> {
    return this.http.put<IMeterImport>(`${this.resourceUrl}/${this.getMeterImportIdentifier(meterImport)}`, meterImport, {
      observe: 'response',
    });
  }

  partialUpdate(meterImport: PartialUpdateMeterImport): Observable<EntityResponseType> {
    return this.http.patch<IMeterImport>(`${this.resourceUrl}/${this.getMeterImportIdentifier(meterImport)}`, meterImport, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMeterImport>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMeterImport[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMeterImportIdentifier(meterImport: Pick<IMeterImport, 'id'>): number {
    return meterImport.id;
  }

  compareMeterImport(o1: Pick<IMeterImport, 'id'> | null, o2: Pick<IMeterImport, 'id'> | null): boolean {
    return o1 && o2 ? this.getMeterImportIdentifier(o1) === this.getMeterImportIdentifier(o2) : o1 === o2;
  }

  addMeterImportToCollectionIfMissing<Type extends Pick<IMeterImport, 'id'>>(
    meterImportCollection: Type[],
    ...meterImportsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const meterImports: Type[] = meterImportsToCheck.filter(isPresent);
    if (meterImports.length > 0) {
      const meterImportCollectionIdentifiers = meterImportCollection.map(
        meterImportItem => this.getMeterImportIdentifier(meterImportItem)!
      );
      const meterImportsToAdd = meterImports.filter(meterImportItem => {
        const meterImportIdentifier = this.getMeterImportIdentifier(meterImportItem);
        if (meterImportCollectionIdentifiers.includes(meterImportIdentifier)) {
          return false;
        }
        meterImportCollectionIdentifiers.push(meterImportIdentifier);
        return true;
      });
      return [...meterImportsToAdd, ...meterImportCollection];
    }
    return meterImportCollection;
  }
}
