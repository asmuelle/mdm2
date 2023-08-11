import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWasteTrackingParameters } from '../waste-tracking-parameters.model';
import { WasteTrackingParametersService } from '../service/waste-tracking-parameters.service';

export const wasteTrackingParametersResolve = (route: ActivatedRouteSnapshot): Observable<null | IWasteTrackingParameters> => {
  const id = route.params['id'];
  if (id) {
    return inject(WasteTrackingParametersService)
      .find(id)
      .pipe(
        mergeMap((wasteTrackingParameters: HttpResponse<IWasteTrackingParameters>) => {
          if (wasteTrackingParameters.body) {
            return of(wasteTrackingParameters.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default wasteTrackingParametersResolve;
