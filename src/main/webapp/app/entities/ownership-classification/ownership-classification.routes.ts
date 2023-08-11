import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OwnershipClassificationComponent } from './list/ownership-classification.component';
import { OwnershipClassificationDetailComponent } from './detail/ownership-classification-detail.component';
import { OwnershipClassificationUpdateComponent } from './update/ownership-classification-update.component';
import OwnershipClassificationResolve from './route/ownership-classification-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const ownershipClassificationRoute: Routes = [
  {
    path: '',
    component: OwnershipClassificationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OwnershipClassificationDetailComponent,
    resolve: {
      ownershipClassification: OwnershipClassificationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OwnershipClassificationUpdateComponent,
    resolve: {
      ownershipClassification: OwnershipClassificationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OwnershipClassificationUpdateComponent,
    resolve: {
      ownershipClassification: OwnershipClassificationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ownershipClassificationRoute;
