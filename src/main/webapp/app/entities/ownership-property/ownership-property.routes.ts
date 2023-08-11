import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OwnershipPropertyComponent } from './list/ownership-property.component';
import { OwnershipPropertyDetailComponent } from './detail/ownership-property-detail.component';
import { OwnershipPropertyUpdateComponent } from './update/ownership-property-update.component';
import OwnershipPropertyResolve from './route/ownership-property-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const ownershipPropertyRoute: Routes = [
  {
    path: '',
    component: OwnershipPropertyComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OwnershipPropertyDetailComponent,
    resolve: {
      ownershipProperty: OwnershipPropertyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OwnershipPropertyUpdateComponent,
    resolve: {
      ownershipProperty: OwnershipPropertyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OwnershipPropertyUpdateComponent,
    resolve: {
      ownershipProperty: OwnershipPropertyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ownershipPropertyRoute;
