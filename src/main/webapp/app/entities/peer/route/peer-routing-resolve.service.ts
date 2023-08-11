import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPeer } from '../peer.model';
import { PeerService } from '../service/peer.service';

export const peerResolve = (route: ActivatedRouteSnapshot): Observable<null | IPeer> => {
  const id = route.params['id'];
  if (id) {
    return inject(PeerService)
      .find(id)
      .pipe(
        mergeMap((peer: HttpResponse<IPeer>) => {
          if (peer.body) {
            return of(peer.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default peerResolve;
