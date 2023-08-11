import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WasteTrackingParametersComponent } from './list/waste-tracking-parameters.component';
import { WasteTrackingParametersDetailComponent } from './detail/waste-tracking-parameters-detail.component';
import { WasteTrackingParametersUpdateComponent } from './update/waste-tracking-parameters-update.component';
import WasteTrackingParametersResolve from './route/waste-tracking-parameters-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const wasteTrackingParametersRoute: Routes = [
  {
    path: '',
    component: WasteTrackingParametersComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WasteTrackingParametersDetailComponent,
    resolve: {
      wasteTrackingParameters: WasteTrackingParametersResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WasteTrackingParametersUpdateComponent,
    resolve: {
      wasteTrackingParameters: WasteTrackingParametersResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WasteTrackingParametersUpdateComponent,
    resolve: {
      wasteTrackingParameters: WasteTrackingParametersResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default wasteTrackingParametersRoute;
