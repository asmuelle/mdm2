import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOwnershipClassification } from '../ownership-classification.model';
import { OwnershipClassificationService } from '../service/ownership-classification.service';

export const ownershipClassificationResolve = (route: ActivatedRouteSnapshot): Observable<null | IOwnershipClassification> => {
  const id = route.params['id'];
  if (id) {
    return inject(OwnershipClassificationService)
      .find(id)
      .pipe(
        mergeMap((ownershipClassification: HttpResponse<IOwnershipClassification>) => {
          if (ownershipClassification.body) {
            return of(ownershipClassification.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default ownershipClassificationResolve;
