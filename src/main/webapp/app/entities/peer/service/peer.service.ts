import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPeer, NewPeer } from '../peer.model';

export type PartialUpdatePeer = Partial<IPeer> & Pick<IPeer, 'id'>;

export type EntityResponseType = HttpResponse<IPeer>;
export type EntityArrayResponseType = HttpResponse<IPeer[]>;

@Injectable({ providedIn: 'root' })
export class PeerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/peers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(peer: NewPeer): Observable<EntityResponseType> {
    return this.http.post<IPeer>(this.resourceUrl, peer, { observe: 'response' });
  }

  update(peer: IPeer): Observable<EntityResponseType> {
    return this.http.put<IPeer>(`${this.resourceUrl}/${this.getPeerIdentifier(peer)}`, peer, { observe: 'response' });
  }

  partialUpdate(peer: PartialUpdatePeer): Observable<EntityResponseType> {
    return this.http.patch<IPeer>(`${this.resourceUrl}/${this.getPeerIdentifier(peer)}`, peer, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPeer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPeer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPeerIdentifier(peer: Pick<IPeer, 'id'>): number {
    return peer.id;
  }

  comparePeer(o1: Pick<IPeer, 'id'> | null, o2: Pick<IPeer, 'id'> | null): boolean {
    return o1 && o2 ? this.getPeerIdentifier(o1) === this.getPeerIdentifier(o2) : o1 === o2;
  }

  addPeerToCollectionIfMissing<Type extends Pick<IPeer, 'id'>>(
    peerCollection: Type[],
    ...peersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const peers: Type[] = peersToCheck.filter(isPresent);
    if (peers.length > 0) {
      const peerCollectionIdentifiers = peerCollection.map(peerItem => this.getPeerIdentifier(peerItem)!);
      const peersToAdd = peers.filter(peerItem => {
        const peerIdentifier = this.getPeerIdentifier(peerItem);
        if (peerCollectionIdentifiers.includes(peerIdentifier)) {
          return false;
        }
        peerCollectionIdentifiers.push(peerIdentifier);
        return true;
      });
      return [...peersToAdd, ...peerCollection];
    }
    return peerCollection;
  }
}
