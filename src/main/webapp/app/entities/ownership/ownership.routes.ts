import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OwnershipComponent } from './list/ownership.component';
import { OwnershipDetailComponent } from './detail/ownership-detail.component';
import { OwnershipUpdateComponent } from './update/ownership-update.component';
import OwnershipResolve from './route/ownership-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const ownershipRoute: Routes = [
  {
    path: '',
    component: OwnershipComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OwnershipDetailComponent,
    resolve: {
      ownership: OwnershipResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OwnershipUpdateComponent,
    resolve: {
      ownership: OwnershipResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OwnershipUpdateComponent,
    resolve: {
      ownership: OwnershipResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ownershipRoute;
