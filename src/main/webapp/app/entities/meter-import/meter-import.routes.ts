import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MeterImportComponent } from './list/meter-import.component';
import { MeterImportDetailComponent } from './detail/meter-import-detail.component';
import { MeterImportUpdateComponent } from './update/meter-import-update.component';
import MeterImportResolve from './route/meter-import-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const meterImportRoute: Routes = [
  {
    path: '',
    component: MeterImportComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MeterImportDetailComponent,
    resolve: {
      meterImport: MeterImportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MeterImportUpdateComponent,
    resolve: {
      meterImport: MeterImportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MeterImportUpdateComponent,
    resolve: {
      meterImport: MeterImportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default meterImportRoute;
