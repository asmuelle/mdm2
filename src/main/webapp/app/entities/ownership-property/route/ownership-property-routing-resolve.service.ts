import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOwnershipProperty } from '../ownership-property.model';
import { OwnershipPropertyService } from '../service/ownership-property.service';

export const ownershipPropertyResolve = (route: ActivatedRouteSnapshot): Observable<null | IOwnershipProperty> => {
  const id = route.params['id'];
  if (id) {
    return inject(OwnershipPropertyService)
      .find(id)
      .pipe(
        mergeMap((ownershipProperty: HttpResponse<IOwnershipProperty>) => {
          if (ownershipProperty.body) {
            return of(ownershipProperty.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default ownershipPropertyResolve;
