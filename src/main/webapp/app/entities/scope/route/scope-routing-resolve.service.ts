import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IScope } from '../scope.model';
import { ScopeService } from '../service/scope.service';

export const scopeResolve = (route: ActivatedRouteSnapshot): Observable<null | IScope> => {
  const id = route.params['id'];
  if (id) {
    return inject(ScopeService)
      .find(id)
      .pipe(
        mergeMap((scope: HttpResponse<IScope>) => {
          if (scope.body) {
            return of(scope.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default scopeResolve;
