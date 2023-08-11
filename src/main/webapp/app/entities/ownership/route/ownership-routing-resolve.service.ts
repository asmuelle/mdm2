import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOwnership } from '../ownership.model';
import { OwnershipService } from '../service/ownership.service';

export const ownershipResolve = (route: ActivatedRouteSnapshot): Observable<null | IOwnership> => {
  const id = route.params['id'];
  if (id) {
    return inject(OwnershipService)
      .find(id)
      .pipe(
        mergeMap((ownership: HttpResponse<IOwnership>) => {
          if (ownership.body) {
            return of(ownership.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default ownershipResolve;
