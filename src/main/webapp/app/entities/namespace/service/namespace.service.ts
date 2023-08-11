import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INamespace, NewNamespace } from '../namespace.model';

export type PartialUpdateNamespace = Partial<INamespace> & Pick<INamespace, 'id'>;

export type EntityResponseType = HttpResponse<INamespace>;
export type EntityArrayResponseType = HttpResponse<INamespace[]>;

@Injectable({ providedIn: 'root' })
export class NamespaceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/namespaces');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(namespace: NewNamespace): Observable<EntityResponseType> {
    return this.http.post<INamespace>(this.resourceUrl, namespace, { observe: 'response' });
  }

  update(namespace: INamespace): Observable<EntityResponseType> {
    return this.http.put<INamespace>(`${this.resourceUrl}/${this.getNamespaceIdentifier(namespace)}`, namespace, { observe: 'response' });
  }

  partialUpdate(namespace: PartialUpdateNamespace): Observable<EntityResponseType> {
    return this.http.patch<INamespace>(`${this.resourceUrl}/${this.getNamespaceIdentifier(namespace)}`, namespace, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INamespace>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INamespace[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getNamespaceIdentifier(namespace: Pick<INamespace, 'id'>): number {
    return namespace.id;
  }

  compareNamespace(o1: Pick<INamespace, 'id'> | null, o2: Pick<INamespace, 'id'> | null): boolean {
    return o1 && o2 ? this.getNamespaceIdentifier(o1) === this.getNamespaceIdentifier(o2) : o1 === o2;
  }

  addNamespaceToCollectionIfMissing<Type extends Pick<INamespace, 'id'>>(
    namespaceCollection: Type[],
    ...namespacesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const namespaces: Type[] = namespacesToCheck.filter(isPresent);
    if (namespaces.length > 0) {
      const namespaceCollectionIdentifiers = namespaceCollection.map(namespaceItem => this.getNamespaceIdentifier(namespaceItem)!);
      const namespacesToAdd = namespaces.filter(namespaceItem => {
        const namespaceIdentifier = this.getNamespaceIdentifier(namespaceItem);
        if (namespaceCollectionIdentifiers.includes(namespaceIdentifier)) {
          return false;
        }
        namespaceCollectionIdentifiers.push(namespaceIdentifier);
        return true;
      });
      return [...namespacesToAdd, ...namespaceCollection];
    }
    return namespaceCollection;
  }
}
