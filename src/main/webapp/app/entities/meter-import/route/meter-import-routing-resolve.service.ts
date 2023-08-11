import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMeterImport } from '../meter-import.model';
import { MeterImportService } from '../service/meter-import.service';

export const meterImportResolve = (route: ActivatedRouteSnapshot): Observable<null | IMeterImport> => {
  const id = route.params['id'];
  if (id) {
    return inject(MeterImportService)
      .find(id)
      .pipe(
        mergeMap((meterImport: HttpResponse<IMeterImport>) => {
          if (meterImport.body) {
            return of(meterImport.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default meterImportResolve;
