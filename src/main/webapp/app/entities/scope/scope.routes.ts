import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ScopeComponent } from './list/scope.component';
import { ScopeDetailComponent } from './detail/scope-detail.component';
import { ScopeUpdateComponent } from './update/scope-update.component';
import ScopeResolve from './route/scope-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const scopeRoute: Routes = [
  {
    path: '',
    component: ScopeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ScopeDetailComponent,
    resolve: {
      scope: ScopeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ScopeUpdateComponent,
    resolve: {
      scope: ScopeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ScopeUpdateComponent,
    resolve: {
      scope: ScopeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default scopeRoute;
