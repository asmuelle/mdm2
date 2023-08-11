import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INamespace } from '../namespace.model';
import { NamespaceService } from '../service/namespace.service';

export const namespaceResolve = (route: ActivatedRouteSnapshot): Observable<null | INamespace> => {
  const id = route.params['id'];
  if (id) {
    return inject(NamespaceService)
      .find(id)
      .pipe(
        mergeMap((namespace: HttpResponse<INamespace>) => {
          if (namespace.body) {
            return of(namespace.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default namespaceResolve;
