import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NamespaceComponent } from './list/namespace.component';
import { NamespaceDetailComponent } from './detail/namespace-detail.component';
import { NamespaceUpdateComponent } from './update/namespace-update.component';
import NamespaceResolve from './route/namespace-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const namespaceRoute: Routes = [
  {
    path: '',
    component: NamespaceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NamespaceDetailComponent,
    resolve: {
      namespace: NamespaceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NamespaceUpdateComponent,
    resolve: {
      namespace: NamespaceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NamespaceUpdateComponent,
    resolve: {
      namespace: NamespaceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default namespaceRoute;
