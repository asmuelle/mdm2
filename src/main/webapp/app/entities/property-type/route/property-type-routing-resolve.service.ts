import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPropertyType } from '../property-type.model';
import { PropertyTypeService } from '../service/property-type.service';

export const propertyTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IPropertyType> => {
  const id = route.params['id'];
  if (id) {
    return inject(PropertyTypeService)
      .find(id)
      .pipe(
        mergeMap((propertyType: HttpResponse<IPropertyType>) => {
          if (propertyType.body) {
            return of(propertyType.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default propertyTypeResolve;
